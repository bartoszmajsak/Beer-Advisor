package org.arquillian.example.functional;

import java.net.URL;

import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.pages.Pages;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.arquillian.example.functional.pages.Beer;
import org.arquillian.example.functional.specs.BeerAdvisor;
import org.arquillian.example.functional.steps.SearchingSteps;
import org.arquillian.example.functional.utils.Deployments;
import org.arquillian.example.thucydides.ArquillianEnricher;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(ThucydidesRunner.class)
@Story(BeerAdvisor.Searching.class)
public class SearchingBeersStory
{

   @Managed(uniqueSession = true)
   public WebDriver webdriver;

   @ManagedPages
   public Pages pages;

   @Steps
   public SearchingSteps searchingSteps;

   @Rule
   public ArquillianEnricher enricher = new ArquillianEnricher();

   @Deployment(testable = false)
   public static WebArchive createTestArchive()
   {
      return Deployments.create();
   }

   @ArquillianResource
   URL deploymentUrl;

   @Before
   public void before_tests()
   {
      pages.setDefaultBaseUrl(deploymentUrl.toExternalForm());
   }

   @Test
   public void should_find_all_swiss_beers()
   {
      searchingSteps.on_main_beer_advisor_page();
      searchingSteps.search_for("from switzerland");
      searchingSteps.should_contain_beers(new Beer("Bügel"), new Beer("Appenzeller Schwarzer Kristall"));
   }

   @Test
   public void should_find_all_belgian_beers()
   {
      searchingSteps.on_main_beer_advisor_page();
      searchingSteps.search_for("from belgium");
      searchingSteps.should_contain_beers( new Beer("Delirium Tremens"), new Beer("Pauwel Kwak"));
   }

   @Pending
   @Test
   public void should_find_cheapest_beer()
   {
   }

}

