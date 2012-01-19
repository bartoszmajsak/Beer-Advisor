package org.arquillian.example.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

/**
 * This test was exported from Selenium IDE. Unfortunately it does not work :)
 *
 * @see BeerAdvisorFixedSeleniumTest
 *
 */
public class BeerAdvisorExportedSeleniumTest extends SeleneseTestCase {

   @Before
    public void setUp() throws Exception {
        WebDriver driver = new FirefoxDriver();
        String baseUrl = "http://localhost:8080/beer-advisor";
        Selenium selenium = new WebDriverBackedSelenium(driver, baseUrl);
        selenium.start();
    }

    @Test
    public void testWebdriver() throws Exception {
        selenium.open("/home.jsf");
        selenium.type("id=advisor:beerSearch", "strongest");
        selenium.keyPress("id=advisor:beerSearch", "\\13");
        for (int second = 0;; second++) {
            if (second >= 60) fail("timeout");
            try { if (selenium.isElementPresent("end_of_history")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertEquals("End of History", selenium.getTable("id=beer-results-table.1.0"));
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}