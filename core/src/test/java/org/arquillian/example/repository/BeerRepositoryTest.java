package org.arquillian.example.repository;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Country;
import org.arquillian.example.repository.BeerRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BeerRepositoryTest
{

   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
                       .addPackage(Beer.class.getPackage())
                       .addPackage(BeerRepository.class.getPackage())
                       .addPackages(true, "org.fest")
                       .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                       .addAsManifestResource("test-persistence.xml", "persistence.xml");
   }

   @Inject
   BeerRepository beerRepository;

   @Test
   @UsingDataSet("beers.yml")
   public void shouldFindStrongestBeer() throws Exception
   {
      // given
      String expectedName = "End of history";
      BigDecimal expectedVoltage = BigDecimal.valueOf(55.0);

      // when
      Set<Beer> beers = beerRepository.strongest();
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getAlcohol()).isEqualByComparingTo(expectedVoltage);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void shouldFindCheapestBeer() throws Exception
   {
      // given
      String expectedName = "Mocny Full";
      BigDecimal expectedPrice = BigDecimal.valueOf(1.0);

      // when
      Set<Beer> beers = beerRepository.cheapest();
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getPrice()).isEqualByComparingTo(expectedPrice);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void shouldFindAllBelgianBeers() throws Exception
   {
      // given
      Country belgium = Country.BELGIUM;

      // when
      Set<Beer> beers = beerRepository.from(belgium);

      // then
      assertThat(beers).hasSize(2);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void shouldFindAllPolishBeers() throws Exception
   {
      // given
      Country poland = Country.POLAND;

      // when
      Set<Beer> beers = beerRepository.from(poland);

      // then
      assertThat(beers).hasSize(1);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void shouldReturnAllBeers() throws Exception
   {
      // given
      int expectedAmountOfBeers = 5;

      // when
      Set<Beer> allBeers = beerRepository.fetchAll();

      // then
      assertThat(allBeers).hasSize(expectedAmountOfBeers);
   }

   @Test
   @UsingDataSet("beers.yml")
   public void shouldReturnBeerByItsId() throws Exception
   {
      // given
      Long beerId = 1L;
      String expectedName = "Mocny Full";

      // when
      Beer beers = beerRepository.getById(beerId);

      // then
      assertThat(beers.getName()).isEqualTo(expectedName);
   }

}
