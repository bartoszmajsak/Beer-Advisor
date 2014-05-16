package org.arquillian.example.repository.mongo;

import org.arquillian.example.domain.mongo.Recipe;

public interface RecipeRepository {

    void save(Recipe recipe);
    Recipe findRecipeByBeerCode(String beerCode);
    
}
