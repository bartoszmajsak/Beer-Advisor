package org.arquillian.example.ui.web;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Main page of the Beer Advisor app.
 *
 * Provides following functionalities:
 * <ul>
 *  <li>Searching for beers using some criteria</li>
 *  <li>Getting details of the selected beer </li>
 * </ul>
 */
public class BeerAdvisorPage
{

   private static final int SECONDS_TO_WAIT = 1;

   private static final int POLL_EVERY_MS = 60;

   private static final String RESULT_TABLE_XPATH = "//table[@id='beer-results-table']/tbody/tr";

   private static final String BEER_LINK = "*//a[text()='%s']";

   private final WebDriver driver;

   private final String baseLocation;

   @FindBy(id = "advisor:beerSearch")
   private WebElement searchBox;

   public BeerAdvisorPage(WebDriver driver, String location)
   {
      this.driver = driver;
      this.baseLocation = location;
      driver.get(location);
      PageFactory.initElements(driver, this);
   }

   public List<Beer> searchFor(String criteria)
   {
      searchBox.clear();
      searchBox.sendKeys(criteria);
      if (driver instanceof FirefoxDriver)
      {
         /*
          * This is workaround for sending "enter key pressed" event to the search box for Firefox browser.
          * Apparently newer versions of Selenium and Firefox are having problem with that.
          * Using selenium wrapper solves this problem.
          */
         new WebDriverBackedSelenium(driver, baseLocation).keyPress(searchBox.getAttribute("id"), "\\13");
      }
      else
      {
         searchBox.sendKeys(Keys.ENTER);
      }
      if (errorDisplayed())
      {
         return Collections.emptyList();
      }
      else
      {
         waitUntilTableContentChanged();
      }

      return transformRowsToBeers();
   }

   /**
    * Gets details of the first beer matching the criteria.
    */
   public Beer detailsOf(String criteria)
   {
      return openDetailsForFirstMatchingBeer(criteria);
   }

   public void errorMessageShouldBeDisplayed(String errorMessage)
   {
      WebElement errorMessageTextField = getErrorMessageTextField();
      assertThat(errorMessageTextField.getText()).isEqualTo(errorMessage);
   }

   public void shouldSuggest(String ... suggestedCriteria)
   {
      final List<String> criteria = extractListElements("available-criteria");
      assertThat(criteria).containsOnly(suggestedCriteria);
   }

   // --- Private methods

   private List<String> extractListElements(final String listId)
   {
      final List<WebElement> listElements = findListElements(listId);
      final List<String> criteria = Lists.transform(listElements, new Function<WebElement, String>() {
         @Override
         public String apply(WebElement li)
         {
            return li.getText();
         }
      });
      return criteria;
   }

   private List<WebElement> findListElements(final String listId)
   {
      return driver.findElement(By.id(listId)).findElements(By.tagName("li"));
   }

   private WebElement getErrorMessageTextField()
   {
      return driver.findElement(By.id("advisor:error"));
   }

   private Beer openDetailsForFirstMatchingBeer(String criteria)
   {
      linkToFirstMatchingBeer(criteria).click();
      return new BeerDetailsPage(driver).extract();
   }

   private WebElement linkToFirstMatchingBeer(String criteria)
   {
      Beer firstBeerMatchingCriteria = searchFor(criteria).get(0);

      final String beerLinkXpath = String.format(BEER_LINK, firstBeerMatchingCriteria.getName());
      final WebElement beerLink = driver.findElement(By.xpath(beerLinkXpath));
      return beerLink;
   }

   private List<Beer> transformRowsToBeers()
   {
      final List<WebElement> foundResultRows = driver.findElements(By.xpath(RESULT_TABLE_XPATH));
      return Lists.transform(foundResultRows, new BeerRowsExtractor());
   }

   /**
    * Beer table content is loaded through AJAX so we
    * need to wait for new content to appear.
    */
   private void waitUntilTableContentChanged()
   {
      final Set<WebElement> originalTableRows = Sets.newHashSet(driver.findElements(By.xpath(RESULT_TABLE_XPATH)));
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

      final WebDriverWait wait = new WebDriverWait(driver, SECONDS_TO_WAIT, POLL_EVERY_MS);
      wait.until(rowsInTableChangedCondition);
   }

   private boolean errorDisplayed()
   {
      try
      {
         final WebDriverWait wait = new WebDriverWait(driver, SECONDS_TO_WAIT, POLL_EVERY_MS);
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
