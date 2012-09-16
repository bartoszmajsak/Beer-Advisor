package org.arquillian.example.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.Selenium;

/**
 * Fixed version of test recorded using Selenium IDE 1.5 and exported
 * as JUnit4 WebDriverBackedSelenium.
 *
 * But this test still have bunch of problems:
 * <ul>
 *  <li>First of all test is written using low level and verbose API, which operates directly in the DOM domain.</li>
 *  <li>This makes it hard to understand when you will go back to it after a while.</li>
 *  <li>But most importantly if you keep writing tests in this style and your UI will be changed you will immediately end up with "maintenance hell"</li>
 *  <li>Another problem is that WebDriver is managed by you and the URL is hardcoded.</li>
 *  <li>This obviously can be externalized to the system properties, but in some cases you might not know URL of your deployed application beforehand (hello cloud!).</li>
 *  <li>It also assumes that you have already built and deployed your application somewhere</li>
 * </ul>
 *
 * @see BeerAdvisorSeleniumWithPageObjectsTest which demonstrates
 * how we can apply PageObject concept to make our tests more readable and easier to maintain.
 *
 */
public class BeerAdvisorSeleniumTest
{

   private Selenium selenium;

   @Before
   public void setUp() throws Exception
   {
      String baseUrl = "http://localhost:8080/beer-advisor";
      selenium = new WebDriverBackedSelenium(new FirefoxDriver(), baseUrl);
   }

   @Test
   public void should_find_strongest_beer() throws Exception
   {
      selenium.open("/search.xhtml");
      selenium.type("id=advisor:beerSearch", "strongest");
      selenium.keyPress("id=advisor:beerSearch", "\\13");
      for (int second = 0;; second++)
      {
         if (second >= 60)
            fail("timeout");
         try
         {
            if (selenium.isElementPresent("end_of_history"))
               break;
         }
         catch (Exception e)
         {
         }
         Thread.sleep(1000);
      }

      assertEquals("End of history", selenium.getTable("id=beer-results-table.1.0"));
   }

   @After
   public void tearDown() throws Exception
   {
      selenium.stop();
   }
}