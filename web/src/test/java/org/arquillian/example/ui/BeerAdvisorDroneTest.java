
package org.arquillian.example.ui;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.arquillian.drone.browser.Firefox;
import org.arquillian.example.ui.utils.BeersAssert;
import org.arquillian.example.ui.utils.Deployments;
import org.arquillian.example.ui.web.Beer;
import org.arquillian.example.ui.web.BeerAdvisorPage;
import org.arquillian.example.ui.web.BeerDetailsPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

/**
 * This example illustrates how Arquillian and Drone extension
 * can help you to skip the build process and deploys your
 * application for you.
 *
 * It also uses <a href="http://code.google.com/p/selenium/wiki/PageObjects">PageObject pattern</a>
 * to model parts of the web application with which tests are interacting. This makes it more readable
 * and easier to maintain when UI is changing.
 *
 * @see BeerAdvisorPage
 * @see BeerDetailsPage
 *
 */
@RunWith(Arquillian.class)
public class BeerAdvisorDroneTest
{
   /**
    * Deploys your web application and executes tests from the client side.
    * testable=false flag means that the deployment package does not contain
    * any tests to be run on the server side.
    *
    * @return WebArchive created by ShrinkWrap which is deployed in the target container
    */
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
   public void setup()
   {
      // make the driver more patient for our VM environments :)
      driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
   }

   @Test
   public void should_find_cheapest_beer()
   {
      // given
      BeerAdvisorPage beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString());

      // when
      List<Beer> beers = beerAdvisor.searchFor("cheapest");

      // then
      assertThat(beers).hasSize(1);
      beers.get(0).shouldBeNamed("Mocny Full");
   }

   @Test
   public void should_find_strongest_beer_details()
   {
      // given
      BeerAdvisorPage beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString());

      // when
      Beer beer = beerAdvisor.detailsOf("strongest");

      // then
      beer.shouldBeNamed("End of history")
          .shouldBeFrom("Brew Dog")
          .shouldCosts(765.0)
          .shouldHaveAlcoholPercentageOf(55.0);
   }


   @Test
   public void should_find_all_swiss_beers()
   {
      // given
      Beer bugel = new Beer("Bügel");
      Beer appenzeller = new Beer("Appenzeller Schwarzer Kristall");
      BeerAdvisorPage beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString());

      // when
      List<Beer> beers = beerAdvisor.searchFor("from switzerland");

      // then
      BeersAssert.assertThat(beers).shouldContain(bugel, appenzeller);
   }

   /**
    * Runs test in the concrete browser implementation
    *
    * @see src/test/resources-glassfish-3/arquillian.xml for the configuration example
    * @param driver
    */
   @Test
   public void should_find_all_belgian_beers(@Drone @Firefox WebDriver driver)
   {
      // given
      Beer delirium = new Beer("Delirium Tremens");
      Beer kwak = new Beer("Pauwel Kwak");
      BeerAdvisorPage beerAdvisor = new BeerAdvisorPage(driver, deploymentUrl.toString());

      // when
      List<Beer> beers = beerAdvisor.searchFor("from belgium");

      // then
      BeersAssert.assertThat(beers).shouldContain(delirium, kwak);
   }

}