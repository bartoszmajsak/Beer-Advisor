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

@Entity
public class Brewery implements Serializable
{

   private static final long serialVersionUID = 1999262068535652596L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "brewery")
   private Set<Beer> beers = new HashSet<Beer>();

   @Basic
   private String name;

   @Enumerated(EnumType.STRING)
   private Country country;

   Brewery()
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

   // Getters and setters

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