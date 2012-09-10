package org.arquillian.example.functional.pages;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represent beer exposed in the UI.
 *
 * Contains handy method to assert tge state (using fluent interface).
 *
 * @see BeerDetailsPage
 */
public class Beer
{

   private final String name;

   private String brewery;

   private BigDecimal alcohol;

   private BigDecimal price;

   public Beer(String name)
   {
      this.name = name;
   }

   public Beer shouldBeNamed(String name)
   {
      assertThat(this.name).isEqualTo(name);
      return this;
   }

   public Beer shouldCosts(Double price)
   {
      assertThat(this.price).isEqualByComparingTo(BigDecimal.valueOf(price));
      return this;
   }

   public Beer shouldHaveAlcoholPercentageOf(Double alcohol)
   {
      assertThat(this.alcohol).isEqualByComparingTo(BigDecimal.valueOf(alcohol));
      return this;
   }

   public Beer shouldBeFrom(String brewery)
   {
      assertThat(this.brewery).isEqualTo(brewery);
      return this;
   }

   @Override
   public int hashCode()
   {
      return new HashCodeBuilder(11, 41).append(name)
                                        .hashCode();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }

      if (!(obj instanceof Beer))
      {
         return false;
      }

      Beer other = (Beer) obj;
      return new EqualsBuilder().append(name, other.getName())
                                .isEquals();
   }

   @Override
   public String toString()
   {
      return new ToStringBuilder(this).append("name", name)
                                      .append("brewery", brewery)
                                      .append("alcohol", alcohol)
                                      .append("price", price)
                                      .toString();
   }

   // Getters and setters

   public String getName()
   {
      return name;
   }

   public String getBrewery()
   {
      return brewery;
   }

   public void setBrewery(String brewery)
   {
      this.brewery = brewery;
   }

   public BigDecimal getAlcohol()
   {
      return alcohol;
   }

   public void setAlcohol(BigDecimal alcohol)
   {
      this.alcohol = alcohol;
   }

   public BigDecimal getPrice()
   {
      return price;
   }

   public void setPrice(BigDecimal price)
   {
      this.price = price;
   }

}
