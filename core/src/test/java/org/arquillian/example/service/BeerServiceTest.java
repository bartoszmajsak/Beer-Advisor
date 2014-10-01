package org.arquillian.example.service;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Country;
import org.arquillian.example.repository.BeerRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

public class BeerServiceTest
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
                       .addPackages(true, Beer.class.getPackage(), BeerRepository.class.getPackage())
                       .addClass(BeerService.class)
                       .addPackages(true, "org.fest")
                       .addPackages(true, "com.google.common.base")
                       .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                       .addAsManifestResource("test-persistence.xml", "persistence.xml");
   }

   @Inject
   BeerService beerService;

   @Test
   @UsingDataSet("beers.yml")
   public void should_return_all_beers() throws Exception
   {
      // given
      int expectedAmountOfBeers = 7;

      // when
      Collection<Beer> allBeers = beerService.fetchByCriteria("ALL");

      // then
      assertThat(allBeers).hasSize(expectedAmountOfBeers);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void should_find_strongest_beer() throws Exception
   {
      // given
      String strongestCriteria = "strongest";
      String expectedName = "End of history";
      BigDecimal expectedVoltage = BigDecimal.valueOf(55.0);

      // when
      Collection<Beer> beers = beerService.fetchByCriteria(strongestCriteria);
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getAlcohol()).isEqualByComparingTo(expectedVoltage);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void should_find_cheapest_beer() throws Exception
   {
      // given
      String expectedName = "Mocny Full";
      String cheapestCriteria = "cheapest";
      BigDecimal expectedPrice = BigDecimal.valueOf(1.0);

      // when
      Collection<Beer> beers = beerService.fetchByCriteria(cheapestCriteria);
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getPrice()).isEqualByComparingTo(expectedPrice);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void should_find_all_belgian_beers() throws Exception
   {
      // given
      String belgiumCriteria = "from Belgium";

      // when
      Collection<Beer> beers = beerService.fetchByCriteria(belgiumCriteria);

      // then
      assertThat(beers).hasSize(2);
      Beer beer = beers.iterator().next();
      assertThat(beer.getBrewery().getCountry()).isEqualTo(Country.BELGIUM);
   }

}
