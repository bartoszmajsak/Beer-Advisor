package org.arquillian.example.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.Expose;

@Entity
public class Brewery implements Serializable
{

   private static final long serialVersionUID = 1999262068535652596L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "brewery", orphanRemoval = true)
   private Set<Beer> beers = new HashSet<Beer>();

   @Basic
   @NotNull
   @Expose
   private String name;

   @Enumerated(EnumType.STRING)
   @NotNull
   @Expose
   private Country country;

   protected Brewery()
   {
      // To satisfy JPA
   }

   public Brewery(String name, Country country)
   {
      this.name = name;
      this.country = country;
   }

   public void addBeer(Beer beer)
   {
      beers.add(beer);
      beer.setBrewery(this);
   }

   public void remove(Beer beer)
   {
      beers.remove(beer);
      beer.setBrewery(null);
   }

   @Override
   public int hashCode()
   {
      final int prime = 17;
      int result = 1;
      result = prime * result + ((country == null) ? 0 : country.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof Brewery))
      {
         return false;
      }
      Brewery other = (Brewery) obj;
      if (country != other.country)
      {
         return false;
      }
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
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

   public Set<Beer> getBeers()
   {
      return Collections.unmodifiableSet(beers);
   }

   public void setBeers(Set<Beer> beers)
   {
      this.beers.addAll(beers);
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Country getCountry()
   {
      return country;
   }

   public void setCountry(Country country)
   {
      this.country = country;
   }

}