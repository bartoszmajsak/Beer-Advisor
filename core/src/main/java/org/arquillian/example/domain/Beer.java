package org.arquillian.example.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.Expose;

@Entity
public class Beer implements Serializable
{
   private static final long serialVersionUID = 5892013208071126314L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Expose
   private Long id;

   @Basic
   @NotNull
   @Expose
   private String name;

   @Basic
   @NotNull
   @Expose
   private BigDecimal price;

   @Basic
   @NotNull
   @Expose
   private BigDecimal alcohol;

   @Basic
   @NotNull
   private String code;

   @Enumerated(EnumType.STRING)
   private Type type;

   @ManyToOne(optional = false, cascade = CascadeType.ALL)
   @Expose
   private Brewery brewery;

   protected Beer()
   {
      // to satisfy JPA
   }

   public Beer(String name, Brewery brewery, Type type, BigDecimal price, BigDecimal alcohol, String code)
   {
      this.name = name;
      this.price = price;
      this.alcohol = alcohol;
      this.type = type;
      this.code = code;
      this.brewery = brewery;
      this.brewery.addBeer(this);
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((code == null) ? 0 : code.hashCode());
      return result;
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
      if (code == null)
      {
         if (other.code != null)
         {
            return false;
         }
      }
      else if (!code.equals(other.code))
      {
         return false;
      }

      return true;
   }

   // --- Accessor methods

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public BigDecimal getPrice()
   {
      return price;
   }

   public void setPrice(BigDecimal price)
   {
      this.price = price;
   }

   public BigDecimal getAlcohol()
   {
      return alcohol;
   }

   public void setAlcohol(BigDecimal alcohol)
   {
      this.alcohol = alcohol;
   }

   public Brewery getBrewery()
   {
      return brewery;
   }

   public void setBrewery(Brewery brewery)
   {
      this.brewery = brewery;
   }

   public Type getType()
   {
      return type;
   }

   public void setType(Type type)
   {
      this.type = type;
   }

   public String getCode()
   {
      return code;
   }

   public void setCode(String code)
   {
      this.code = code;
   }

}