package org.arquillian.example.ui

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver
import org.arquillian.example.ui.web.Beer
import org.arquillian.example.ui.web.BeerAdvisor
import org.arquillian.example.ui.utils.BeersAssert


description "Beer Advisor killing search features"

using 'WebDriver'

scenario "Searching for all Swiss beers using natural language", {

       given "On the main page", {
          bugel = new Beer("Bügel")
          appenzeller = new Beer("Appenzeller Schwarzer Kristall")
          beerAdvisor = new BeerAdvisor(driver, "http://beeradvisor-bmajsak.rhcloud.com/")
       }

       when "Enter 'from switzerland' as the search criteria", {
          beers = beerAdvisor.searchFor("from switzerland")

       }

       then "You should see all Swiss beers", {
          BeersAssert.assertThat(beers).shouldContain(bugel, appenzeller)
       }

}

scenario "Searching for all Belgian beers using natural language", {

       given "On the main page", {
          delirium = new Beer("Delirium Tremens")
          kwak = new Beer("Pauwel Kwak")
          beerAdvisor = new BeerAdvisor(driver, "http://beeradvisor-bmajsak.rhcloud.com/")
       }

       when "Enter 'from belgium' as the search criteria", {
          beers = beerAdvisor.searchFor("from belgium")

       }

       then "You should see all Belgian beers", {
          BeersAssert.assertThat(beers).shouldContain(delirium, kwak)
       }

}

scenario "Details of the strongest beer", {

       given "On the main page", {
          expectedBeerName = "End of history"
          expectedBrewery = "Brew Dog"
          expectedPrice = BigDecimal.valueOf(765.0)
          expectedAlcohol = BigDecimal.valueOf(55.0)

          beerAdvisor = new BeerAdvisor(driver, "http://beeradvisor-bmajsak.rhcloud.com/");
       }

       when "Enter 'strongest' as the search criteria and click on the beer", {
          beer = beerAdvisor.detailsOf("strongest")
       }

       then "You should see detailed information about the strongest beer in the world", {
          beer.shouldBeNamed(expectedBeerName)
              .shouldBeFrom(expectedBrewery)
              .shouldCost(expectedPrice)
              .shouldHaveAlcoholPercentageOf(expectedAlcohol)
       }

}
