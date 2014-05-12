package org.arquillian.example.repository;

import javax.inject.Inject;

import org.arquillian.example.domain.Recipe;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoDBRecipeRepository implements RecipeRepository {

    @Inject
    @RecipeCollection
    DBCollection recipeCollection;
    
    @Override
    public void save(Recipe recipe) 
    {
        DBObject recipeDBObject = recipe.toDBObject();
        recipeCollection.insert(recipeDBObject);
    }

    @Override
    public Recipe findRecipeByBeerCode(String beerCode) {
        
        DBObject query = new BasicDBObject(Recipe.BEER_CODE, beerCode);
        DBObject recipeDBObject = recipeCollection.findOne(query);
        
        if(recipeDBObject != null)
        {
            return Recipe.fromDBObject(recipeDBObject);
        }
        
        return null;
    }

}