package org.arquillian.example.service;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.repository.BeerRepository;
import org.arquillian.example.service.BeerService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Data;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
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
   @Data("datasets/beers.yml")
   public void shouldReturnAllBeers() throws Exception
   {
      // given
      int expectedAmountOfBeers = 5;

      // when
      Set<Beer> allBeers = beerService.fetchByCriteria("ALL");

      // then
      assertThat(allBeers).hasSize(expectedAmountOfBeers);
   }

   @Test
   @Data("datasets/beers.yml")
   public void shouldFindStrongestBeer() throws Exception
   {
      // given
      String strongestCriteria = "strongest";
      String expectedName = "End of history";
      BigDecimal expectedVoltage = BigDecimal.valueOf(55.0);

      // when
      Set<Beer> beers = beerService.fetchByCriteria(strongestCriteria);
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getAlcohol()).isEqualByComparingTo(expectedVoltage);
   }

   @Test
   @Data("datasets/beers.yml")
   public void shouldFindCheapestBeer() throws Exception
   {
      // given
      String expectedName = "Mocny Full";
      String cheapestCriteria = "cheapest";
      BigDecimal expectedPrice = BigDecimal.valueOf(1.0);

      // when
      Set<Beer> beers = beerService.fetchByCriteria(cheapestCriteria);
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getPrice()).isEqualByComparingTo(expectedPrice);
   }

   @Test
   @Data("datasets/beers.yml")
   public void shouldFindAllBelgianBeers() throws Exception
   {
      // given
      String belgiumCriteria = "from Belgium";

      // when
      Set<Beer> beers = beerService.fetchByCriteria(belgiumCriteria);

      // then
      assertThat(beers).hasSize(2);
   }

}
