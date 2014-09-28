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
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.arquillian.example.ui.utils.BeersAssert
import org.arquillian.example.ui.utils.SpockDeployments
import org.arquillian.example.ui.web.BeerAdvisorPage
import spock.lang.*

/**
 *
 * Plain Spock acceptance test for Beer Advisor leveraging WebDriver and its
 * implementation of Page Objects.
 */
class BeerAdvisorPlainSpockSpec extends Specification
{
   @Shared def driver
   @Shared def baseUrl

   def setupSpec()
   {
      driver = new FirefoxDriver();
      baseUrl = "http://localhost:8080/beer-advisor"
   }

   @Test
   def "Finding strongest beer using search criteria"()
   {
      given: "I'm on the main page"
         def beerAdvisor = new BeerAdvisorPage(driver, baseUrl)
         def strongestBeerSearchCriteria = "strongest"
         def expectedBeerName = "End of history"

      when: "I enter '${strongestBeerSearchCriteria}' as the search criteria and click on the beer"
         def beers = beerAdvisor.searchFor strongestBeerSearchCriteria

      then: "I should see the strongest beer ever created called ${expectedBeerName}"
         assertThat(beers).hasSize(1)
         beers.get(0).shouldBeNamed(expectedBeerName)
   }

   def cleanupSpec()
   {
      driver.close();
   }

}
