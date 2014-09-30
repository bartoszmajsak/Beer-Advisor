package org.arquillian.example.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.arquillian.example.domain.Beer;
import org.arquillian.example.repository.BeerRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Path("beer")
@Stateless
public class BeerResource
{

   @Inject
   private BeerRepository beerRepository;

   @GET
   @Path("{code}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getBeerByCode(@PathParam("code") String code)
   {
      final Beer beer = beerRepository.getByCode(code);
      final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
      return Response.ok(gson.toJson(beer)).build();
   }

   @DELETE
   @Path("{code}")
   public Response deleteById(@PathParam("code") String code)
   {

      beerRepository.delete(beerRepository.getByCode(code));
      return Response.noContent().build();
   }

}
