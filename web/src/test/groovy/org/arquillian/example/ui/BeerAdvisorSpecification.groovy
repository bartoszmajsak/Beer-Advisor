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
import org.arquillian.example.ui.web.BeerAdvisorPage
import spock.lang.*

/**
 *
 * Acceptance tests for Beer Advisor app covering
 * its search capabilities. Tests can be treated as
 * executable specifications. They are written
 * in <a href="">gherkin language</a>
 *
 */
@Stepwise
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
   def "Finding all belgian beers"()
   {
      given: "I'm on the main page"
         def delirium = new Beer("Delirium Tremens")
         def kwak = new Beer("Pauwel Kwak")
         def beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I enter 'from belgium' as the search criteria"
         def beers = beerAdvisor.searchFor("from belgium")

      then: "I should see Delirium and Kwak"
         BeersAssert.assertThat(beers).shouldContain(delirium, kwak)
   }

   @Test
   def "Cheapest beer"()
   {
      given: "I'm on the main page"
         def expectedBeerName = "Mocny Full"
         def beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I search for the 'cheapest' beer"
         def beers = beerAdvisor.searchFor("cheapest")

      then: "I should see the best Polish beer ever created ;)"
         assertThat(beers).hasSize(1)
         beers.get(0).shouldBeNamed(expectedBeerName)
   }

   @Test
   def "Should find details of the strongest beer"()
   {
      given: "I'm on the main page"
         def beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I enter 'strongest' as the search criteria and click on the beer"
         def beer = beerAdvisor.detailsOf("strongest")

      then: "I should see detailed information about the strongest beer in the world"
         beer.shouldBeNamed("End of history")
             .shouldBeFrom("Brew Dog")
             .shouldCost(765.0)
             .shouldHaveAlcoholPercentageOf(55.0)
   }

   @Test
   def "Should display error message when no result found for criteria"()
   {
      given: "I'm on the main page"
         def beerAdvisorPage = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I enter a criteria for which there is no result"
         def beers = beerAdvisorPage.searchFor("aAa")

      then: "Error message should be displayed"
         Thread.sleep(1000)
         beerAdvisorPage.errorMessageShouldBeDisplayed("No beers matching specified criteria 'aAa'")
   }

}
