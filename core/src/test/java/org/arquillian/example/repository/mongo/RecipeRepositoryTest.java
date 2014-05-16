package org.arquillian.example.repository.mongo;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.mongo.Recipe;
import org.arquillian.example.repository.BeerRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.persistence.UsingDataSets;
import org.jboss.arquillian.persistence.dbunit.DBUnit;
import org.jboss.arquillian.persistence.nosqlunit.mongodb.MongoDB;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
public class RecipeRepositoryTest {

    @Deployment
    public static Archive<?> createDeployment() {

        Archive<?> polyglot = ShrinkWrap.create(JavaArchive.class)
                .addPackage(Beer.class.getPackage())
                .addPackage(BeerRepository.class.getPackage())
                .addPackage(Recipe.class.getPackage())
                .addPackages(true, Filters.exclude(RecipeRepositoryBasicTest.class),
                           RecipeRepository.class.getPackage())
                .addPackages(true, "org.fest")
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        return polyglot;

    }

    @Inject
    private BeerRepository beerRepository;

    @Inject
    private RecipeRepository recipeRepository;

    @Test
    @UsingDataSets({
        @UsingDataSet(value = "beers.yml", backend = DBUnit.class),
        @UsingDataSet(value = "mongo/recipies.json", backend = MongoDB.class)
    })
    public void should_find_cheapest_beer_and_its_recipe() throws Exception {
        // given
        String expectedName = "Mocny Full";
        BigDecimal expectedPrice = BigDecimal.valueOf(1.0);

        // when
        Set<Beer> beers = beerRepository.cheapest();
        Beer cheapestBeer = beers.iterator().next();
        Recipe recipe = recipeRepository.findRecipeByBeerCode(cheapestBeer.getCode());

        // then
        assertThat(cheapestBeer.getName()).isEqualTo(expectedName);
        assertThat(recipe.getBeerCode()).isEqualTo(cheapestBeer.getCode());
    }

}
