package org.arquillian.example.ui

import static org.fest.assertions.Assertions.assertThat

import java.net.URL
import java.util.List
import java.util.concurrent.TimeUnit

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

   def setup()
   {
      // make the driver more patient for our VM environments :)
      driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)
   }

   @Test
   def "Should find all Swiss beers"()
   {
      given: "On the main page"
         def bugel = new Beer("Bügel")
         def appenzeller = new Beer("Appenzeller Schwarzer Kristall")
         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when: "Enter 'from switzerland' as the search criteria"
         def beers = beerAdvisor.searchFor("from switzerland")

      then: "You should see all Swiss beers"
         BeersAssert.assertThat(beers).shouldContain(bugel, appenzeller)
   }

   @Test
   def "Should find all belgian beers"()
   {
      given: "On the main page"
         def delirium = new Beer("Delirium Tremens")
         def kwak = new Beer("Pauwel Kwak")
         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when: "Enter 'from belgium' as the search criteria"
         def beers = beerAdvisor.searchFor("from belgium")

      then: "You should see all Belgian beers"
         BeersAssert.assertThat(beers).shouldContain(delirium, kwak)
   }

   @Test
   def "Should find cheapest beer"()
   {
      given: "On the main page"
         def expectedBeerName = "Mocny Full"
         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when: "Enter 'cheapest' as the search criteria and click on the beer"
         def beers = beerAdvisor.searchFor("cheapest")

      then: "You should see the best Polish beer ever created ;)"
         assertThat(beers).hasSize(1)
         beers.get(0).shouldBeNamed(expectedBeerName)
   }

   @Test
   def "Should find details of the strongest beer"()
   {
      given: "On the main page"
         def expectedBeerName = "End of history"
         def expectedBrewery = "Brew Dog"
         def expectedPrice = BigDecimal.valueOf(765.0)
         def expectedAlcohol = BigDecimal.valueOf(55.0)

         def beerAdvisor = new BeerAdvisor(driver, deploymentUrl.toString())

      when: "Enter 'strongest' as the search criteria and click on the beer"
         def beer = beerAdvisor.detailsOf("strongest")

      then: "You should see detailed information about the strongest beer in the world"
         beer.shouldBeNamed(expectedBeerName)
             .shouldBeFrom(expectedBrewery)
             .shouldCost(expectedPrice)
             .shouldHaveAlcoholPercentageOf(expectedAlcohol)
   }
}
