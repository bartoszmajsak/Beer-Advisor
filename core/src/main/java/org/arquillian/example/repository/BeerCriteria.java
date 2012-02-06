package org.arquillian.example.repository;

import org.arquillian.example.repository.exception.UnknownBeerCriteriaException;

import com.google.common.base.Preconditions;


public enum BeerCriteria
{
   ALL("all"),
   CHEAPEST("cheapest"),
   STRONGEST("strongest"),
   BELGIUM("from belgium"),
   SWITZERLAND("from switzerland"),
   NONE("none");

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
      for (BeerCriteria beerCriteria : values())
      {
         if (criteriaString.equals(beerCriteria.criteriaString))
         {
            return beerCriteria;
         }
      }
      throw new UnknownBeerCriteriaException("Cannot resolve criteria for given string representation: '" + criteriaString + "'");
   }

}
