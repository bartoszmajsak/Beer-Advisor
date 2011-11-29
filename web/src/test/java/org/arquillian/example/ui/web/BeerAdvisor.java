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

public class BeerAdvisor
{

   private static final int SECONDS_TO_WAIT = 2;

   private static final int POLL_EVERY_MS = 15;

   private static final String RESULT_TABLE_XPATH = "//table[@id='beer-results-table']/tbody/tr";

   private static final String BEER_LINK = "*//a[text()='%s']";

   private final WebDriver driver;

   @FindBy(id = "advisor:beerSearch")
   WebElement searchBox;

   public BeerAdvisor(WebDriver driver, String location)
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

      final List<WebElement> foundResultRows = driver.findElements(By.xpath(RESULT_TABLE_XPATH));
      return Lists.transform(foundResultRows, new BeerRowsExtractor());
   }

   public Beer detailsOf(String criteria)
   {
      Beer strongest = searchFor(criteria).get(0);

      String beerLinkXpath = String.format(BEER_LINK, strongest.getName());
      WebElement beerLink = driver.findElement(By.xpath(beerLinkXpath));

      beerLink.click();

      BeerDetails beerDetails = PageFactory.initElements(driver, BeerDetails.class);
      return beerDetails.getBeer();
   }

   // Private methods

   private void waitUntilTableContentChanged()
   {
      final List<WebElement> tableRows = driver.findElements(By.xpath(RESULT_TABLE_XPATH));
      ExpectedCondition<Boolean> rowsInTableChangedCondition = new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            final List<WebElement> currentTableRows = input.findElements(By.xpath(RESULT_TABLE_XPATH));
            boolean domTableContentChanged = !Sets.newHashSet(tableRows).contains(Sets.newHashSet(currentTableRows));
            return domTableContentChanged;
         }
      };
      WebDriverWait wait = new WebDriverWait(driver, SECONDS_TO_WAIT, POLL_EVERY_MS);
      wait.until(rowsInTableChangedCondition);
   }

}
