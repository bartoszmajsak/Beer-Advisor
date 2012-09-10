package org.arquillian.example.functional.pages;

import java.util.List;
import java.util.Set;

import net.thucydides.core.pages.PageObject;
import net.thucydides.core.webdriver.WebDriverFacade;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Main page of the Beer Advisor app.
 *
 * Provides following functionalities:
 * <ul>
 *  <li>Searching for beers using some criteria</li>
 *  <li>Getting details of the selected beer</li>
 * </ul>
 */
public class BeerAdvisorPage extends PageObject
{

   private static final int SECONDS_TO_WAIT = 1;

   private static final int POLL_EVERY_MS = 60;

   private static final String RESULT_TABLE_XPATH = "//table[@id='beer-results-table']/tbody/tr";

   private static final String BEER_LINK = "*//a[text()='%s']";

   @FindBy(id = "advisor:beerSearch")
   private WebElement searchBox;

   public BeerAdvisorPage(WebDriver driver)
   {
      super(driver);
   }

   public void searchFor(String criteria)
   {
      searchBox.clear();
      searchBox.sendKeys(criteria);
      if (((WebDriverFacade) getDriver()).getProxiedDriver() instanceof FirefoxDriver)
      {
         /*
          * This is workaround for sending "enter key pressed" event to the search box for Firefox browser.
          * Apparently newer versions of Selenium and Firefox are having problem with that.
          * Using selenium wrapper solves this problem.
          */
         new WebDriverBackedSelenium(getDriver(), getDriver().getCurrentUrl()).keyPress(searchBox.getAttribute("id"), "\\13");
      }
      else
      {
         searchBox.sendKeys(Keys.ENTER);
      }

      if (!errorDisplayed())
      {
         waitUntilTableContentChanged();
      }
   }

   public List<Beer> getListedBeers()
   {
      final List<WebElement> foundResultRows = getDriver().findElements(By.xpath(RESULT_TABLE_XPATH));
      return Lists.transform(foundResultRows, new BeerRowsExtractor());
   }

   public void detailsOf(String criteria)
   {
      linkToFirstMatchingBeer(criteria).click();
   }

   // --- Private methods

   private WebElement linkToFirstMatchingBeer(String criteria)
   {
      searchFor(criteria);
      Beer firstBeerMatchingCriteria = getListedBeers().get(0);

      final String beerLinkXpath = String.format(BEER_LINK, firstBeerMatchingCriteria.getName());
      final WebElement beerLink = getDriver().findElement(By.xpath(beerLinkXpath));
      return beerLink;
   }


   /**
    * Beer table content is loaded through AJAX so we
    * need to wait for new content to appear.
    */
   private void waitUntilTableContentChanged()
   {
      final Set<WebElement> originalTableRows = Sets.newHashSet(getDriver().findElements(By.xpath(RESULT_TABLE_XPATH)));
      final ExpectedCondition<Boolean> rowsInTableChangedCondition = new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            final List<WebElement> currentTableRows = input.findElements(By.xpath(RESULT_TABLE_XPATH));
            boolean domTableContentChanged = !originalTableRows.contains(Sets.newHashSet(currentTableRows));
            return domTableContentChanged;
         }
      };

      final WebDriverWait wait = new WebDriverWait(getDriver(), SECONDS_TO_WAIT, POLL_EVERY_MS);
      wait.until(rowsInTableChangedCondition);
   }

   private boolean errorDisplayed()
   {
      try
      {
         final WebDriverWait wait = new WebDriverWait(getDriver(), SECONDS_TO_WAIT, POLL_EVERY_MS);
         wait.until(ExpectedConditions.presenceOfElementLocated(By.id("advisor:error")));
         return true;
      }
      catch (NoSuchElementException e)
      {
         return false;
      }
      catch (TimeoutException e)
      {
         return false;
      }
   }

}
