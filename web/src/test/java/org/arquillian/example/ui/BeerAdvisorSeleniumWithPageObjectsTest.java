package org.arquillian.example.ui;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.arquillian.example.ui.web.Beer;
import org.arquillian.example.ui.web.BeerAdvisorPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Improved version of {@link BeerAdvisorSeleniumTest} levaring concept of page objects.
 *
 * Concerns still not addressed:
 * <ul>
 *  <li>WebDriver is still managed by you and the URL is hardcoded.</li>
 *  <li>This obviously can be externalized to the system properties, but in some cases you might not know URL of your deployed application beforehand (hello cloud!).</li>
 *  <li>It also assumes that you have already built and deployed your application somewhere</li>
 * </ul>
 *
 * @see BeerAdvisorDroneTest Let's have a look how Arquillian and it's Drone extension
 * can help us to address these limitations.
 *
 */
@Ignore("As the url is fixed so not really automation friendly.")
public class BeerAdvisorSeleniumWithPageObjectsTest
{

   private String baseUrl;

   private WebDriver driver;

   @Before
   public void setUp() throws Exception
   {
      baseUrl = "http://localhost:8080/beer-advisor";
      driver = new FirefoxDriver();
   }

   @Test
   public void should_find_strongest_beer() throws Exception
   {
      // given
      BeerAdvisorPage beerAdvisor = new BeerAdvisorPage(driver, baseUrl);

      // when
      List<Beer> beers = beerAdvisor.searchFor("strongest");

      // then
      assertThat(beers).hasSize(1);
      beers.get(0).shouldBeNamed("End of history");
   }

   @After
   public void tearDown() throws Exception
   {
      driver.close();
   }
}