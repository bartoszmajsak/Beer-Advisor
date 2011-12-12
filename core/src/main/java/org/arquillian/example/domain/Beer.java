package org.arquillian.example.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Beer implements Serializable
{
   private static final long serialVersionUID = 5892013208071126314L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @Basic
   private String name;

   @Basic
   private BigDecimal price;

   @Basic
   private BigDecimal alcohol;

   @Basic
   private String code;

   @Enumerated(EnumType.STRING)
   private Type type;

   // If lazy then Glassfish Embedded is facing this problem: https://bugs.eclipse.org/bugs/show_bug.cgi?id=323403
   @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   private Brewery brewery;

   Beer()
   {
      // to satisfy JPA
   }

   public Beer(Brewery brewery, Type type, String name, BigDecimal price, BigDecimal alcohol)
   {
      this.name = name;
      this.price = price;
      this.alcohol = alcohol;
      this.type = type;
      this.brewery = brewery;
      this.brewery.addBeer(this);
   }

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