package org.arquillian.example.ui

import org.arquillian.example.ui.utils.BeersAssert
import org.arquillian.example.ui.utils.SpockDeployments
import org.arquillian.example.ui.web.Beer
import org.arquillian.example.ui.web.BeerAdvisorPage
import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.drone.api.annotation.Drone
import org.jboss.arquillian.test.api.ArquillianResource
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.Test
import org.openqa.selenium.WebDriver
import spock.lang.Specification
import spock.lang.Stepwise

import java.util.concurrent.TimeUnit

import static org.fest.assertions.Assertions.assertThat

/**
 *
 * Acceptance tests for Beer Advisor app covering
 * its search capabilities. Tests can be treated as
 * executable specifications. They are written
 * in <a href="http://en.wikipedia.org/wiki/Behavior_Driven_Development#Application_examples_in_the_Gherkin_language">gherkin language</a>.
 */
@Stepwise
class BeerAdvisorSpecification extends Specification
{

   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      return SpockDeployments.create()
   }

   @ArquillianResource
   URL deploymentUrl

   @Drone
   WebDriver driver

   def setup()
   {
      // make the driver more patient for our VM environments :)
      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
   }

   @Test
   def "Finding all belgian beers"()
   {
      given: "I'm on the main page"
         def delirium = new Beer("Delirium Tremens")
         def kwak = new Beer("Pauwel Kwak")
         def beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I enter 'from belgium' as the search criteria"
         def beers = beerAdvisor.searchFor "from belgium"

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
         def beers = beerAdvisor.searchFor "cheapest"

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
         def beer = beerAdvisor.detailsOf "strongest"

      then: "I should see detailed information about the strongest beer in the world"
         beer.shouldBeNamed("End of history")
             .shouldBeFrom("Brew Dog")
             .shouldCosts(765.0)
             .shouldHaveAlcoholPercentageOf(55.0)
   }

   @Test
   def "Should display error message when no result found for criteria"()
   {
      given: "I'm on the main page"
         def beerAdvisorPage = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I enter a criteria for which there is no result"
         def beers = beerAdvisorPage.searchFor "aAa"

      then: "Error message should be displayed"
         beerAdvisorPage.errorMessageShouldBeDisplayed "No beers matching specified criteria 'aAa'."
   }

   @Test
   def "Should display available criteria when wrong criteria have been entered"()
   {
      given: "I'm on the main page"
         def beerAdvisorPage = new BeerAdvisorPage(driver, deploymentUrl.toString())

      when: "I enter a criteria for which there is no result"
         def beers = beerAdvisorPage.searchFor "aAa"

      then: "All available criteria should be suggested"
         beerAdvisorPage.shouldSuggest "all", "cheapest", "strongest", "from belgium", "from norway", "from switzerland", "from sweden"
   }

}
