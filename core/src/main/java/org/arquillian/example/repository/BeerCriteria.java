package org.arquillian.example.repository;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.base.Preconditions;

public enum BeerCriteria
{
   ALL("all"),
   CHEAPEST("cheapest"),
   STRONGEST("strongest"),
   BELGIUM("from belgium"),
   SWITZERLAND("from switzerland"),
   NONE("none"),
   UNKNOWN("---");

   private final String criteriaString;

   private BeerCriteria(String criteriaString)
   {
      this.criteriaString = criteriaString;
   }

   public static BeerCriteria fromStringDescription(String criteriaString)
   {
      Preconditions.checkNotNull(criteriaString, "Criteria string should not be null");
      Preconditions.checkArgument(!criteriaString.trim().isEmpty(), "Criteria string should not be empty");

      criteriaString = criteriaString.trim().toLowerCase();

      final Set<BeerCriteria> handledCriteria = EnumSet.complementOf(EnumSet.of(UNKNOWN));
      for (BeerCriteria beerCriteria : handledCriteria)
      {
         if (criteriaString.equals(beerCriteria.criteriaString))
         {
            return beerCriteria;
         }
      }
      return UNKNOWN;
   }

}
