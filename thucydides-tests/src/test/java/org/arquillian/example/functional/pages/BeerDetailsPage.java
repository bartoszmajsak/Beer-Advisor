package org.arquillian.example.functional.pages;

import java.math.BigDecimal;

import net.thucydides.core.pages.PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object encapsulating "beer details" page.
 */
public class BeerDetailsPage extends PageObject
{

   @FindBy(id = "brewery")
   private WebElement breweryElement;

   @FindBy(id = "name")
   private WebElement nameElement;

   @FindBy(className = "voltage")
   private WebElement alcoholElement;

   @FindBy(id = "price")
   private WebElement priceElement;

   public BeerDetailsPage(WebDriver driver)
   {
      super(driver);
   }

   public Beer extract()
   {
      final Beer beer = new Beer(nameElement.getText());
      beer.setBrewery(breweryElement.getText().trim());
      beer.setAlcohol(extractAlcohol());
      beer.setPrice(extractPrice());
      return beer;
   }

   // --- Private methods

   private BigDecimal extractPrice()
   {
      String priceText = priceElement.getText();
      priceText = priceText.replaceAll("Price: ", "").replace('$', ' ').trim();
      BigDecimal price = BigDecimal.valueOf(Double.valueOf(priceText));
      return price;
   }

   private BigDecimal extractAlcohol()
   {
      String alcoholText = alcoholElement.getText().trim();
      alcoholText = alcoholText.replaceAll("%", "");
      BigDecimal alcohol = BigDecimal.valueOf(Double.valueOf(alcoholText));
      return alcohol;
   }

}
