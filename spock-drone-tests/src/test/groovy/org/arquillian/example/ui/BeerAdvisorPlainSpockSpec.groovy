package org.arquillian.example.ui

import org.arquillian.example.ui.web.BeerAdvisorPage
import org.junit.Test
import org.openqa.selenium.firefox.FirefoxDriver
import spock.lang.Shared
import spock.lang.Specification

import static org.fest.assertions.Assertions.assertThat

/**
 *
 * Plain Spock acceptance test for Beer Advisor leveraging WebDriver and its
 * implementation of Page Objects.
 */
class BeerAdvisorPlainSpockSpec extends Specification {
    @Shared def driver
    @Shared def baseUrl

    def setupSpec() {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080/beer-advisor"
    }

    @Test
    def "Finding strongest beer using search criteria"() {
        given: "I'm on the main page"
        def beerAdvisor = new BeerAdvisorPage(driver, baseUrl)
        def strongestBeerSearchCriteria = "strongest"
        def expectedBeerName = "End of history"

        when:
        "I enter '${strongestBeerSearchCriteria}' as the search criteria and click on the beer"
        def beers = beerAdvisor.searchFor strongestBeerSearchCriteria

        then:
        "I should see the strongest beer ever created called ${expectedBeerName}"
        assertThat(beers).hasSize(1)
        beers.get(0).shouldBeNamed(expectedBeerName)
    }

    def cleanupSpec() {
        driver.close();
    }
}
