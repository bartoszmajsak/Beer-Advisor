package org.arquillian.example.repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Beer_;
import org.arquillian.example.domain.Brewery;
import org.arquillian.example.domain.Brewery_;
import org.arquillian.example.domain.Country;

@RequestScoped
public class JpaBeerRepository implements BeerRepository
{
   @PersistenceContext
   private EntityManager em;

   @Override
   public void delete(Beer beer)
   {
      beer.getBrewery().remove(beer);
      em.remove(beer);
   }

   @Override
   public void delete(Long id)
   {
      em.remove(getById(id));
   }

   @Override
   public Beer getById(Long id)
   {
      return em.find(Beer.class, id);
   }

   @Override
   public Set<Beer> cheapest()
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Beer> criteriaQuery = criteriaBuilder.createQuery(Beer.class);
      Root<Beer> fromBeers = criteriaQuery.from(Beer.class);
      Predicate strongestBeerPredicate = criteriaBuilder.equal(fromBeers.get(Beer_.price), lowestPrice());
      criteriaQuery.select(fromBeers).where(strongestBeerPredicate);

      Set<Beer> result = new HashSet<Beer>();
      result.addAll(em.createQuery(criteriaQuery).getResultList());

      return result;
   }

   @Override
   public Set<Beer> strongest()
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Beer> criteriaQuery = criteriaBuilder.createQuery(Beer.class);
      Root<Beer> fromBeers = criteriaQuery.from(Beer.class);
      Predicate strongestBeerPredicate = criteriaBuilder.equal(fromBeers.get(Beer_.alcohol),
            strongestAlcoholPercentage());
      criteriaQuery.select(fromBeers).where(strongestBeerPredicate);

      final Set<Beer> result = new HashSet<Beer>();
      result.addAll(em.createQuery(criteriaQuery).getResultList());

      return result;

   }

   @Override
   public Beer getByCode(String code)
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Beer> query = criteriaBuilder.createQuery(Beer.class);
      Root<Beer> beers = query.from(Beer.class);

      Predicate codeEquals = criteriaBuilder.equal(beers.get(Beer_.code), code);
      CriteriaQuery<Beer> select = query.select(beers).where(codeEquals);

      try
      {
         return em.createQuery(select).getSingleResult();
      }
      catch (NoResultException e)
      {
         return null;
      }
   }

   @Override
   public Set<Beer> from(Country country)
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Beer> query = criteriaBuilder.createQuery(Beer.class);
      Root<Beer> beers = query.from(Beer.class);

      Predicate isFromCountry = criteriaBuilder.equal(beers.get(Beer_.brewery).get(Brewery_.country), country);
      CriteriaQuery<Beer> select = query.select(beers).where(isFromCountry);

      final Set<Beer> result = new HashSet<Beer>();
      result.addAll(em.createQuery(select).getResultList());

      return result;
   }

   @Override
   public Set<Beer> fetchAll()
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Beer> query = criteriaBuilder.createQuery(Beer.class);
      Root<Beer> from = query.from(Beer.class);
      CriteriaQuery<Beer> select = query.select(from);

      final Set<Beer> result = new HashSet<Beer>();
      result.addAll(em.createQuery(select).getResultList());

      return result;
   }

   @Override
   public void save(Beer beer)
   {
      if (beer.getId() == null)
      {
         em.persist(beer);
      }
      else
      {
         em.merge(beer);
      }
   }

   @Override
   public void save(Brewery brewery)
   {
      if (brewery.getId() == null)
      {
         em.persist(brewery);
      }
      else
      {
         em.merge(brewery);
      }

   }

   // Private methods

   private BigDecimal strongestAlcoholPercentage()
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<BigDecimal> query = criteriaBuilder.createQuery(BigDecimal.class);
      Root<Beer> beerRoot = query.from(Beer.class);
      Expression<BigDecimal> maxAlcohol = criteriaBuilder.max(beerRoot.get(Beer_.alcohol));

      return em.createQuery(query.select(maxAlcohol)).getSingleResult();
   }

   private BigDecimal lowestPrice()
   {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<BigDecimal> query = criteriaBuilder.createQuery(BigDecimal.class);
      Root<Beer> beerRoot = query.from(Beer.class);
      Expression<BigDecimal> minPrice = criteriaBuilder.min(beerRoot.get(Beer_.price));

      return em.createQuery(query.select(minPrice)).getSingleResult();
   }
}