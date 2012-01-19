package org.arquillian.example.repository;

import java.util.Set;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Brewery;
import org.arquillian.example.domain.Country;

public interface BeerRepository
{

   void save(Beer beer);

   void save(Brewery brewery);

   Beer getById(Long id);

   Set<Beer> cheapest();

   Set<Beer> strongest();

   Set<Beer> from(Country country);

   Set<Beer> fetchAll();

}