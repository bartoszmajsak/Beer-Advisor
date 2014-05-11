package org.arquillian.example.repository;

import org.arquillian.example.domain.Recipe;

public interface RecipeRepository {

    void save(Recipe recipe);
    Recipe findRecipeByBeerCode(String beerCode);
    
}
