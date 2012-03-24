package org.arquillian.example.service;

import java.util.Collections;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Country;
import org.arquillian.example.repository.BeerCriteria;
import org.arquillian.example.repository.BeerRepository;

@RequestScoped
public class BeerService
{

   @Inject
   private BeerRepository beerRepository;

   public Set<Beer> fetchByCriteria(String criteriaString)
   {
      final BeerCriteria criteria = BeerCriteria.fromStringDescription(criteriaString);

      switch (criteria)
      {
         case ALL:
            return beerRepository.fetchAll();
         case SWITZERLAND:
            return beerRepository.from(Country.SWITZERLAND);
         case BELGIUM:
            return beerRepository.from(Country.BELGIUM);
         case CHEAPEST:
            return beerRepository.cheapest();
         case STRONGEST:
            return beerRepository.strongest();
         case NONE:
         case UNKNOWN:
            return Collections.emptySet();
      }

      return Collections.emptySet();

   }

   public Beer getById(Long id)
   {
      return beerRepository.getById(id);
   }

   public Set<Beer> fetchAll()
   {
      return beerRepository.fetchAll();
   }

}
