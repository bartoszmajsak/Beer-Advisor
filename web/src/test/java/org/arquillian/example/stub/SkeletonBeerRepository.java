package org.arquillian.example.stub;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.BeerBuilder;
import org.arquillian.example.domain.Brewery;
import org.arquillian.example.domain.Country;
import org.arquillian.example.domain.Type;
import org.arquillian.example.repository.BeerRepository;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

/**
 * Until we implement repository/dao layer we can use
 * this stub for keeping our acceptance (end-to-end) tests green.
 *
 * @see Deployments
 */
@ApplicationScoped
@Alternative
public class SkeletonBeerRepository implements BeerRepository
{
   private Map<Long, Beer> beers = new HashMap<Long, Beer>();

   private Beer strongest;

   private Beer cheapest;

   @Override
   public Beer getById(Long id)
   {
      return beers.get(id);
   }

   @Override
   public Set<Beer> cheapest()
   {
      return Sets.newHashSet(cheapest);
   }

   @Override
   public Set<Beer> strongest()
   {
      return Sets.newHashSet(strongest);
   }

   @Override
   public Set<Beer> from(final Country country)
   {
      final Collection<Beer> beersFromSpecifiedCountry = Collections2.filter(beers.values(), new Predicate<Beer>() {

         @Override
         public boolean apply(Beer beer)
         {
            return beer.getBrewery().getCountry().equals(country);
         }

      });

      return Sets.newHashSet(beersFromSpecifiedCountry);
   }

   @Override
   public Set<Beer> fetchAll()
   {
      return Sets.newHashSet(beers.values());
   }

   @Override
   public void save(Beer beer)
   {
      if (beer.getId() == null)
      {
         beer.setId(Long.valueOf(beers.size() + 1));
      }
      beers.put(beer.getId(), beer);
   }

   @Override
   public void save(Brewery brewery)
   {
   }

   // ----------------------------------------

   @PostConstruct
   public void initializeBeerRepository()
   {
      Beer mocnyFull = BeerBuilder.create().named("Mocny Full")
                                    .withPrice(BigDecimal.valueOf(1.0))
                                    .havingAlcohol(BigDecimal.valueOf(4.5))
                                    .from(new Brewery("Kiepski Browar", Country.POLAND))
                                    .ofType(Type.LAGER)
                                    .withCode("mocny_full")
                                    .build();
      save(mocnyFull);
      cheapest = mocnyFull;

      Brewery brewDog = new Brewery("Brew Dog", Country.SCOTLAND);
      Beer endOfHistory = BeerBuilder.create().named("End of history")
                                       .withPrice(BigDecimal.valueOf(765.0))
                                       .havingAlcohol(BigDecimal.valueOf(55.0))
                                       .from(brewDog)
                                       .ofType(Type.BLOND_ALE)
                                       .withCode("end_of_history")
                                       .build();

      Beer bismarck = BeerBuilder.create().named("Sink The Bismarck!")
                                   .withPrice(BigDecimal.valueOf(64.0))
                                   .havingAlcohol(BigDecimal.valueOf(41.0))
                                   .from(brewDog)
                                   .ofType(Type.QUADRUPEL_IPA)
                                   .withCode("bismarck")
                                   .build();
      save(endOfHistory);
      save(bismarck);
      save(brewDog);
      strongest = endOfHistory;

      Beer delirium = BeerBuilder.create().named("Delirium Tremens")
                                   .withPrice(BigDecimal.valueOf(10.0))
                                   .havingAlcohol(BigDecimal.valueOf(8.5))
                                   .from(new Brewery("Brouwerij Huyghe", Country.BELGIUM))
                                   .ofType(Type.PALE_ALE)
                                   .withCode("delirium")
                                   .build();
      save(delirium);

      Beer kwak = BeerBuilder.create().named("Pauwel Kwak")
                               .withPrice(BigDecimal.valueOf(4.0))
                               .havingAlcohol(BigDecimal.valueOf(8.4))
                               .from(new Brewery("Brouwerij Bosteels", Country.BELGIUM))
                               .ofType(Type.AMBER)
                               .withCode("kwak")
                               .build();
      save(kwak);

      Beer bugel = BeerBuilder.create().named("Bügel")
                                .withPrice(BigDecimal.valueOf(3.0))
                                .havingAlcohol(BigDecimal.valueOf(4.8))
                                .from(new Brewery("Feldschlösschen", Country.SWITZERLAND))
                                .ofType(Type.VIENNA)
                                .withCode("bugel")
                                .build();
      save(bugel);

      Beer appenzellerSchwarzKrystall = BeerBuilder.create().named("Appenzeller Schwarzer Kristall")
            .withPrice(BigDecimal.valueOf(4.0))
            .havingAlcohol(BigDecimal.valueOf(6.3))
            .from(new Brewery("Locher", Country.SWITZERLAND))
            .ofType(Type.SCHWARZBIER)
            .withCode("schwarzer_kristall")
            .build();
      save(appenzellerSchwarzKrystall);
   }

}