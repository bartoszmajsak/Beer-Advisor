package org.arquillian.example.rest;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import org.arquillian.example.ui.utils.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;

@RunWith(Arquillian.class)
@RunAsClient
public class BeerResourceTest
{

   @Deployment
   public static WebArchive createDeployment()
   {
      return Deployments.create();
   }

   @ArquillianResource
   private URL applicationPath;

   @Before
   public void resourcePath()
   {
       RestAssured.baseURI = applicationPath.toString();
       RestAssured.basePath = "/resource/beer";
   }

   @Test
   public void should_return_beer_based_on_id()
   {
      expect()
              .statusCode(equalTo(200))
              .body("id", equalTo(1),
                    "name", equalTo("Mocny Full"))
      .given()
              .request().pathParameter("id", 1)
      .when()
              .get("/{id}");
   }

   @Test
   public void should_delete_beer_based_on_id()
   {
      expect()
              .statusCode(equalTo(204))
      .given()
              .request().pathParameter("id", 1)
      .when()
              .delete("/{id}");
   }

}
