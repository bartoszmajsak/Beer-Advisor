package org.arquillian.example.repository.mongo;

import static com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb.InMemoryMongoRuleBuilder.newInMemoryMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.arquillian.example.domain.mongo.Recipe;
import org.arquillian.example.repository.mongo.MongoDBRecipeRepository;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.InMemoryMongoDb;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;


public class RecipeRepositoryBasicTest
{

    private static final String TEST_DB = "test";

    @ClassRule
    public static final InMemoryMongoDb IN_MEMORY_MONGO_DB = newInMemoryMongoDbRule().build();

    @Rule
    public MongoDbRule embedMongoDbRule = newMongoDbRule().defaultEmbeddedMongoDb(TEST_DB);
    
    @Inject
    private Mongo mongo;
    
    @Test
    @UsingDataSet(locations="initialRecipes.json", loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
    public void should_return_recipes_by_name() 
    {
        MongoDBRecipeRepository mongoDBRecipeRepository = new MongoDBRecipeRepository();
        mongoDBRecipeRepository.recipeCollection = getRecipesCollection();
        
        Recipe recipe = mongoDBRecipeRepository.findRecipeByBeerCode("kölsch");
        
        assertThat(recipe.getBeerCode(), is("kölsch"));
        assertThat(recipe.getElaboration(), is("Typical English Beer. Original Density 1048 (5% Vol. Alc), ... "));
        assertThat(recipe.getMalts(), contains("Pilsner  4,6kg", "Wheat 800gr"));
        assertThat(recipe.getHops(), contains("Hallertau Hersbrucker Flor 50gr"));
        assertThat(recipe.getYeasts(), contains("Safale S-04"));
        assertThat(recipe.getFiningAgents(), contains("Irish Moss 10gr"));
        
    }
    
    private DBCollection getRecipesCollection() {
        DB db = mongo.getDB(TEST_DB);
        return db.getCollection("recipes");
    }
    
}
