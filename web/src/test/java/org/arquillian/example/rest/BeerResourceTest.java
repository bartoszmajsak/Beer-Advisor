package org.arquillian.example.rest;

import com.jayway.restassured.RestAssured;
import java.net.URL;
import org.arquillian.example.ui.utils.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jayway.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Arquillian.class)
@RunAsClient
public class BeerResourceTest {

    @ArquillianResource
    private URL applicationPath;

    @Deployment
    public static WebArchive createDeployment() {
        return Deployments.create();
    }

    @Before
    public void resourcePath() {
        RestAssured.baseURI = applicationPath.toString();
        RestAssured.basePath = "/resource/beer";
    }

    @Test
    @InSequence(1)
    public void should_return_beer_based_on_unique_code() {
        given()
            .request().pathParameter("code", "mocny_full")
            .when()
            .get("/{code}")
            .then().log().all()
            .statusCode(equalTo(OK.getStatusCode()))
            .body("code", equalTo("mocny_full"),
                "name", equalTo("Mocny Full"));
    }

    @Test
    @InSequence(2)
    public void should_delete_beer_based_on_uniqe_code() {
        given()
            .request().pathParameter("code", "mocny_full")
            .when()
            .delete("/{code}")
            .then()
            .statusCode(equalTo(NO_CONTENT.getStatusCode()));
    }
}
