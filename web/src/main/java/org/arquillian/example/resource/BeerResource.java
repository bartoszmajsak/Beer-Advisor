package org.arquillian.example.resource;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.service.BeerService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("beer")
@Stateless
public class BeerResource
{

   @Inject
   BeerService beerService;

   @GET
   @Path("{id}")
   @Produces("application/json")
   public String getBeerById(@PathParam("id") Long id)
   {
      final Beer beer = beerService.getById(id);
      final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
      return gson.toJson(beer);
   }

}
