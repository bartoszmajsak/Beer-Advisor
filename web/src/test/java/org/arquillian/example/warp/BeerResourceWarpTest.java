package org.arquillian.example.warp;

import static com.jayway.restassured.RestAssured.expect;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import javax.inject.Inject;

import org.arquillian.example.repository.BeerRepository;
import org.arquillian.example.ui.utils.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;

@RunWith(Arquillian.class)
@WarpTest @RunAsClient
public class BeerResourceWarpTest
{

   @Deployment(testable = true)
   public static WebArchive createDeployment()
   {
      return Deployments.create().addPackages(true, "org.fest");
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
   public void should_delete_beer_based_on_id()
   {
      Warp.initiate(new Activity() {
         @Override
         public void perform()
         {
            expect()
                  .statusCode(equalTo(204))
            .given()
                  .request().pathParameter("id", 1)
            .when()
                  .delete("/{id}");
         }
      })
      .inspect(new Inspection() {
         private static final long serialVersionUID = -778115683463909014L;

         @Inject
         private BeerRepository beerRepository;

         @AfterServlet
         public void check() {
            assertThat(beerRepository.getById(1L)).isNull();
         }
      });
   }

}
