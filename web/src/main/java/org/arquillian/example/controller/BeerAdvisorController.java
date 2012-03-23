package org.arquillian.example.controller;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.service.BeerService;

import com.google.common.collect.Lists;

@Named
@RequestScoped
public class BeerAdvisorController
{

   @Inject
   BeerService beerService;

   private String filter;

   public List<Beer> getBeers()
   {
      if (filter == null || filter.trim().isEmpty())
      {
         filter = "none";
      }
      final Set<Beer> result = beerService.fetchByCriteria(filter);
      return Lists.newArrayList(result);
   }

   public String getFilter()
   {
      return filter;
   }

   public void setFilter(String filter)
   {
      this.filter = filter;
   }

}
