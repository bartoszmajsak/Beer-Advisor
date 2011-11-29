package org.arquillian.example.ui.web;

import java.math.BigDecimal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BeerDetails
{

   @FindBy(id = "brewery")
   private WebElement breweryElement;

   @FindBy(id = "name")
   private WebElement nameElement;

   @FindBy(className = "voltage")
   private WebElement alcoholElement;

   @FindBy(id = "price")
   private WebElement priceElement;

   private WebDriver driver;

   public BeerDetails(WebDriver driver)
   {
      this.driver = driver;
   }

   public Beer getBeer()
   {
      Beer beer = new Beer(nameElement.getText());
      beer.setBrewery(breweryElement.getText().trim());
      beer.setAlcohol(extractAlcohol());
      beer.setPrice(extractPrice());
      return beer;
   }

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
