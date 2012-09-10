package org.arquillian.example.functional.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

class BeerRowsExtractor implements Function<WebElement, Beer>
{
   private static final String BEER_NAME = "td/a";

   @Override
   public Beer apply(WebElement input)
   {
      final WebElement description = input.findElement(By.xpath(BEER_NAME));
      return new Beer(description.getText());
   }
}