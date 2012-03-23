package org.arquillian.example.ui.web;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

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

   private static final int SECONDS_TO_WAIT = 2;

   private static final int POLL_EVERY_MS = 15;

   private static final String RESULT_TABLE_XPATH = "//table[@id='beer-results-table']/tbody/tr";

   private static final String BEER_LINK = "*//a[text()='%s']";

   private final WebDriver driver;

   @FindBy(id = "advisor:beerSearch")
   private WebElement searchBox;

   public BeerAdvisorPage(WebDriver driver, String location)
   {
      this.driver = driver;
      driver.get(location);
      PageFactory.initElements(driver, this);
   }

   public List<Beer> searchFor(String criteria)
   {
      searchBox.sendKeys(criteria);
      searchBox.sendKeys(Keys.ENTER);

      waitUntilTableContentChanged();

      return transformRowsToBeers();
   }

   /**
    * Gets details of the first beer matching the criteria.
    */
   public Beer detailsOf(String criteria)
   {
      return openDetailsForFirstMatchingBeer(criteria);
   }

   // --- Private methods

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
      final List<WebElement> tableRows = driver.findElements(By.xpath(RESULT_TABLE_XPATH));
      final ExpectedCondition<Boolean> rowsInTableChangedCondition = new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            final List<WebElement> currentTableRows = input.findElements(By.xpath(RESULT_TABLE_XPATH));
            boolean domTableContentChanged = !Sets.newHashSet(tableRows).contains(Sets.newHashSet(currentTableRows));
            return domTableContentChanged;
         }
      };

      final WebDriverWait wait = new WebDriverWait(driver, SECONDS_TO_WAIT, POLL_EVERY_MS);
      wait.until(rowsInTableChangedCondition);
   }

}
