package org.arquillian.example.repository.mongo;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.arquillian.example.domain.mongo.Recipe;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class RecipeTest 
{

    @Test
    public void should_serialize_recipes_to_json() 
    {
        
        Recipe recipe = new Recipe("kölsch", "Typical English Beer. Original Density 1048 (5% Vol. Alc), ... ");
        recipe.addMalt("Pilsner  4,6kg");
        recipe.addMalt("Wheat 800gr");
        recipe.addHop("Hallertau Hersbrucker Flor 50gr");
        recipe.addYeast("Safale S-04");
        recipe.addFiningAgent("Irish Moss 10gr");
        
        BasicDBObject dbObject = recipe.toDBObject();
        String json = JSON.serialize(dbObject);
        
        with(json).assertThat("$.beer_code", is("kölsch"))
                  .and()
                  .assertThat("$.ingredients.malts", contains("Pilsner  4,6kg", "Wheat 800gr"))
                  .and()
                  .assertThat("$.ingredients.hops", contains("Hallertau Hersbrucker Flor 50gr"))
                  .and()
                  .assertThat("$.ingredients.yeasts", contains("Safale S-04"))
                  .and()
                  .assertThat("$.ingredients.fining_agents", contains("Irish Moss 10gr"))
                  .and()
                  .assertThat("$.elaboration", is("Typical English Beer. Original Density 1048 (5% Vol. Alc), ... "));
    }
    
    @Test
    public void should_create_recipe_from_dbobject()
    {
        
        DBObject dbObject = (DBObject) JSON.parse("{ \"beer_code\" : \"kölsch\" , \"ingredients\" : { \"malts\" : [ \"Pilsner  4,6kg\" , \"Wheat 800gr\"] , \"hops\" : [ \"Hallertau Hersbrucker Flor 50gr\"] , \"yeasts\" : [ \"Safale S-04\"] , \"fining_agents\" : [ \"Irish Moss 10gr\"]} , \"elaboration\" : \"Typical English Beer. Original Density 1048 (5% Vol. Alc), ... \"}");
        
        Recipe recipe = Recipe.fromDBObject(dbObject);
        
        assertThat(recipe.getBeerCode(), is("kölsch"));
        assertThat(recipe.getElaboration(), is("Typical English Beer. Original Density 1048 (5% Vol. Alc), ... "));
        assertThat(recipe.getMalts(), contains("Pilsner  4,6kg", "Wheat 800gr"));
        assertThat(recipe.getHops(), contains("Hallertau Hersbrucker Flor 50gr"));
        assertThat(recipe.getYeasts(), contains("Safale S-04"));
        assertThat(recipe.getFiningAgents(), contains("Irish Moss 10gr"));
        
    }
    
}
