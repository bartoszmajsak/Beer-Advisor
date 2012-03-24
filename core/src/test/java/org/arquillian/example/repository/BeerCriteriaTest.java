package org.arquillian.example.repository;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class BeerCriteriaTest
{

   @Test(expected = NullPointerException.class)
   public void shouldThrowExceptionIfCriteriaStringIsNull() throws Exception
   {
      // given
      String criteriaString = null;

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      // exception should be thrown
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionIfCriteriaStringIsEmpty() throws Exception
   {
      // given
      String criteriaString = "";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      // exception should be thrown
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionIfCriteriaStringIsBlank() throws Exception
   {
      // given
      String criteriaString = "                     ";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      // exception should be thrown
   }

   @Test
   public void shouldReturnUnknownIfCriteriaStringIsNotRecognized() throws Exception
   {
      // given
      String criteriaString = "everything";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      assertThat(beerCriteria).isEqualTo(BeerCriteria.UNKNOWN);
   }

   @Test
   public void shouldRecognizeAllBeerCriteria() throws Exception
   {
      // given
      String criteriaString = "all";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      assertThat(beerCriteria).isEqualTo(BeerCriteria.ALL);
   }

   @Test
   public void shouldRecognizeBelgiumBeerCriteria() throws Exception
   {
      // given
      String criteriaString = "from Belgium    ";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      assertThat(beerCriteria).isEqualTo(BeerCriteria.BELGIUM);
   }

   @Test
   public void shouldRecognizeStrongestBeerCriteria() throws Exception
   {
      // given
      String criteriaString = "   STRONGEST   ";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      assertThat(beerCriteria).isEqualTo(BeerCriteria.STRONGEST);
   }

   @Test
   public void shouldRecognizeCheapestBeerCriteria() throws Exception
   {
      // given
      String criteriaString = "cheapest";

      // when
      BeerCriteria beerCriteria = BeerCriteria.fromStringDescription(criteriaString);

      // then
      assertThat(beerCriteria).isEqualTo(BeerCriteria.CHEAPEST);
   }
}
