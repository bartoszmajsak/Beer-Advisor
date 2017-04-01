package org.arquillian.example.repository;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BeerCriteriaTest {

    @Test(expected = NullPointerException.class)
    public void should_throw_exception_if_criteria_string_is_null() throws Exception {
        // given
        String criteriaString = null;

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        // exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_if_criteria_string_is_empty() throws Exception {
        // given
        String criteriaString = "";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        // exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_if_criteria_string_is_blank() throws Exception {
        // given
        String criteriaString = "                     ";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        // exception should be thrown
    }

    @Test
    public void should_return_unknown_if_criteria_string_is_not_recognized() throws Exception {
        // given
        String criteriaString = "everything";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        assertThat(beerCriteria).isEqualTo(BeerCriteria.UNKNOWN);
    }

    @Test
    public void should_recognize_all_beer_criteria() throws Exception {
        // given
        String criteriaString = "all";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        assertThat(beerCriteria).isEqualTo(BeerCriteria.ALL);
    }

    @Test
    public void should_recognize_belgium_beer_criteria() throws Exception {
        // given
        String criteriaString = "from Belgium    ";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        assertThat(beerCriteria).isEqualTo(BeerCriteria.BELGIUM);
    }

    @Test
    public void should_recognize_strongest_beer_criteria() throws Exception {
        // given
        String criteriaString = "   STRONGEST   ";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        assertThat(beerCriteria).isEqualTo(BeerCriteria.STRONGEST);
    }

    @Test
    public void should_recognize_cheapest_beer_criteria() throws Exception {
        // given
        String criteriaString = "cheapest";

        // when
        BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

        // then
        assertThat(beerCriteria).isEqualTo(BeerCriteria.CHEAPEST);
    }
}
