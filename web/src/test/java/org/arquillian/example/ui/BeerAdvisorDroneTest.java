
package org.arquillian.example.ui;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.arquillian.example.ui.utils.BeersAssert;
import org.arquillian.example.ui.utils.Deployments;
import org.arquillian.example.ui.web.Beer;
import org.arquillian.example.ui.web.BeerAdvisor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@RunAsClient
public class BeerAdvisorDroneTest
{
   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      return Deployments.create();
   }

   @ArquillianResource
   URL deploymentUrl;

   @Drone
   WebDriver driver;

   @Before
   public void config() {
	   // make the driver more patient for our VM environments :)
	   driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
   }
   
   @Test
   public void shouldFindCheapestBeer()
   {
      // given
      final String expectedBeerName = "Mocny Full";
      BeerAdvisor beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString());

      // when
      List<Beer> beers = beerAdvisor.searchFor("cheapest");

      // then
      assertThat(beers).hasSize(1);
      beers.get(0).shouldBeNamed(expectedBeerName);
   }

   @Test
   public void shouldFindAllBelgianBeers()
   {
      // given
      Beer delirium = new Beer("Delirium Tremens");
      Beer kwak = new Beer("Pauwel Kwak");
      BeerAdvisor beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString());

      // when
      List<Beer> beers = beerAdvisor.searchFor("from belgium");

      // then
      BeersAssert.assertThat(beers).shouldContain(delirium, kwak);
   }

   @Test
   public void shouldFindStrongestBeerDetails()
   {
      // given
      final String expectedBeerName = "End of history";
      final String expectedBrewery = "Brew Dog";
      final BigDecimal expectedPrice = BigDecimal.valueOf(765.0);
      final BigDecimal expectedAlcohol = BigDecimal.valueOf(55.0);

      BeerAdvisor beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString());

      // when
      Beer beer = beerAdvisor.detailsOf("strongest");

      // then
      beer.shouldBeNamed(expectedBeerName)
          .shouldBeFrom(expectedBrewery)
          .shouldCost(expectedPrice)
          .shouldHaveAlcoholPercentageOf(expectedAlcohol);
   }
}