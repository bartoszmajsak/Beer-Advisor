package org.arquillian.example.ui

import java.util.List;

import org.openqa.selenium.firefox.FirefoxDriver
import org.arquillian.example.domain.Type;
import org.arquillian.example.ui.web.Beer
import org.arquillian.example.ui.web.BeerAdvisorPage
import org.arquillian.example.ui.utils.BeersAssert

description "Beer Advisor killing search features"

using "WebDriver", "webdriver" // loads easyb plugin and make it available as property

webdriver.configure { driver ->
   driver.type = "chrome"
}

scenario "Searching for all Swiss beers using natural language", {

       given "I am on the main page", {
          bugel = new Beer("Bügel")
          appenzeller = new Beer("Appenzeller Schwarzer Kristall")
          beerAdvisorPage = new BeerAdvisorPage(driver, "http://localhost:8080/beer-advisor")
       }

       when "I enter 'from switzerland' as the search criteria", {
          beers = beerAdvisorPage.searchFor("from switzerland")
       }

       then "I should see all Swiss beers", {
          BeersAssert.assertThat(beers).shouldContain(bugel, appenzeller)
       }

}

scenario "Searching for all Belgian beers using natural language", {

       given "I am on the main page", {
          delirium = new Beer("Delirium Tremens")
          kwak = new Beer("Pauwel Kwak")
          beerAdvisorPage = new BeerAdvisorPage(driver, "http://beeradvisor-bmajsak.rhcloud.com/")
       }

       when "I enter 'from belgium' as the search criteria", {
          beers = beerAdvisorPage.searchFor("from belgium")
       }

       then "I should see all Belgian beers", {
          BeersAssert.assertThat(beers).shouldContain(delirium, kwak)
       }

}

scenario "Details of the strongest beer", {

       given "I am on the main page", {
          beerAdvisorPage = new BeerAdvisorPage(driver, "http://beeradvisor-bmajsak.rhcloud.com/");
       }

       when "I enter 'strongest' as the search criteria and click on the beer", {
          beer = beerAdvisorPage.detailsOf("strongest")
       }

       then "I should see detailed information about the strongest beer in the world", {
          beer.shouldBeNamed("End of history")
              .shouldBeFrom("Brew Dog")
              .shouldCost(765.0)
              .shouldHaveAlcoholPercentageOf(55.0)
       }

}
