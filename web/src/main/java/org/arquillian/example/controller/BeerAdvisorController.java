package org.arquillian.example.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.service.BeerService;

import com.google.common.collect.Lists;

@Named
@RequestScoped
public class BeerAdvisorController
{

   private static final String NONE = "none";

   private boolean notFound;

   private List<Beer> beers = Collections.emptyList();

   @Inject
   private BeerService beerService;

   private String criteria;

   public void loadBeers(AjaxBehaviorEvent event)
   {
      if (criteria == null || criteria.trim().isEmpty())
      {
         criteria = NONE;
      }

      final Set<Beer> result = beerService.fetchByCriteria(criteria);
      notFound = result.isEmpty() && !NONE.equals(criteria);
      beers = Lists.newArrayList(result);
   }

   public List<Beer> getBeers()
   {
      return beers;
   }

   public String getCriteria()
   {
      return criteria;
   }

   public void setCriteria(String filter)
   {
      this.criteria = filter;
   }

   public boolean isNotFound()
   {
      return notFound;
   }

   public void setNotFound(boolean notFound)
   {
      this.notFound = notFound;
   }

}
