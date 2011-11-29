package org.arquillian.example.ui.utils;

import java.util.List;

import org.arquillian.example.ui.web.Beer;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class BeersAssert extends GenericAssert<BeersAssert, List<Beer>>
{

   private BeersAssert(List<Beer> actual)
   {
      super(BeersAssert.class, actual);
   }

   public static BeersAssert assertThat(List<Beer> beers)
   {
      return new BeersAssert(beers);
   }

   public BeersAssert shouldContain(Beer ... beers)
   {
      Assertions.assertThat(actual).contains(beers);
      return this;
   }

   /*
    *    public BeersAssert shouldContain(Beer ... beers)
   {


      return this;
   }
    */

}
