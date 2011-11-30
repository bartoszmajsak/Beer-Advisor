package org.arquillian.example.ui

import static org.fest.assertions.Assertions.assertThat

import java.net.URL
import java.util.List

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.container.test.api.RunAsClient
import org.jboss.arquillian.drone.api.annotation.Drone
import org.jboss.arquillian.test.api.ArquillianResource
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.Test
import org.openqa.selenium.WebDriver

import org.arquillian.example.ui.utils.BeersAssert
import org.arquillian.example.ui.utils.Deployments
import org.arquillian.example.ui.web.Beer
import org.arquillian.example.ui.web.BeerAdvisor
import spock.lang.*

@RunAsClient
class BeerAdvisorSpecification extends Specification
{
   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      return Deployments.create()
   }

   @ArquillianResource
   URL deploymentUrl

   @Drone
   WebDriver driver

   @Test
   def "Should find all belgian beers"()
   {
      given:
         def delirium = new Beer("Delirium Tremens")
         def kwak = new Beer("Pauwel Kwak")
         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when:
         def beers = beerAdvisor.searchFor("from belgium")

      then:
         BeersAssert.assertThat(beers).shouldContain(delirium, kwak)
   }

   @Test
   def "Should find cheapest beer"()
   {
      given:
         def expectedBeerName = "Mocny Full"
         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when:
         def beers = beerAdvisor.searchFor("cheapest")

      then:
         assertThat(beers).hasSize(1)
         beers.get(0).shouldBeNamed(expectedBeerName)
   }

   @Test
   def "Should find details of the strongest beer"()
   {
      given:
         def expectedBeerName = "End of history"
         def expectedBrewery = "Brew Dog"
         def expectedPrice = BigDecimal.valueOf(765.0)
         def expectedAlcohol = BigDecimal.valueOf(55.0)

         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when:
         def beer = beerAdvisor.detailsOf("strongest")

      then:
         beer.shouldBeNamed(expectedBeerName)
             .shouldBeFrom(expectedBrewery)
             .shouldCost(expectedPrice)
             .shouldHaveAlcoholPercentageOf(expectedAlcohol)
   }
}
