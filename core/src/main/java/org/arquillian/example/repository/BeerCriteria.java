package org.arquillian.example.repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public enum BeerCriteria
{
   ALL("all"),
   CHEAPEST("cheapest"),
   STRONGEST("strongest"),
   BELGIUM("from belgium"),
   NORWAY("from norway"),
   SWITZERLAND("from switzerland"),
   NONE("none"),
   UNKNOWN("---");

   private final String criteriaString;

   private BeerCriteria(String criteriaString)
   {
      this.criteriaString = criteriaString;
   }

   public String getCriteriaString()
   {
      return criteriaString;
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

   public static List<BeerCriteria> allCriteria()
   {
      return Lists.newArrayList(EnumSet.complementOf(EnumSet.of(NONE, UNKNOWN)));
   }

}
