package org.arquillian.example.domain;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class Recipe {

    public static final String BEER_CODE = "beer_code";

    private static final long serialVersionUID = 1607244372557373969L;

    //Code defined in code field of org.arquillian.example.domain.Beer entity
    private String beerCode;
    private String elaboration;
    
    private List<String> malts = new ArrayList<String>();
    private List<String> hops = new ArrayList<String>();
    private List<String> yeasts = new ArrayList<String>();
    private List<String> finingAgents = new ArrayList<String>();
    
    protected Recipe() {
        
    }
    
    public Recipe(String beerCode, String elaboration)
    {
        this.beerCode = beerCode;
        this.elaboration = elaboration;
    }
    
    public void addMalt(String maltIngredient)
    {
        this.malts.add(maltIngredient);
    }
    
    public void addHop(String hopIngredient) 
    {
        this.hops.add(hopIngredient);
    }
    
    public void addYeast(String yeastIngredient) {
        this.yeasts.add(yeastIngredient);
    }
    
    public void addFiningAgent(String finingAgent)
    {
        this.finingAgents.add(finingAgent);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getBeerCode() {
        return beerCode;
    }

    public String getElaboration() {
        return elaboration;
    }

    public List<String> getMalts() {
        return malts;
    }

    public List<String> getHops() {
        return hops;
    }

    public List<String> getYeasts() {
        return yeasts;
    }

    public List<String> getFiningAgents() {
        return finingAgents;
    }
    
    public BasicDBObject toDBObject() 
    {
        BasicDBObject recipe = new BasicDBObject();
        recipe.put(BEER_CODE, this.beerCode);
        
        BasicDBList malts = new BasicDBList();
        malts.addAll(this.malts);
        
        BasicDBList hops = new BasicDBList();
        hops.addAll(this.hops);
        
        BasicDBList yeasts = new BasicDBList();
        yeasts.addAll(this.yeasts);
        
        BasicDBList finingAgents = new BasicDBList();
        finingAgents.addAll(this.finingAgents);
        
        BasicDBObject ingredients = new BasicDBObject();
        ingredients.put("malts", malts);
        ingredients.put("hops", hops);
        ingredients.put("yeasts", yeasts);
        ingredients.put("fining_agents", finingAgents);
        
        recipe.put("ingredients", ingredients);
        
        recipe.put("elaboration", this.elaboration);
        
        return recipe;
    }
    
    public static Recipe fromDBObject(DBObject dbObject) 
    {
        String beerCode = (String) dbObject.get(BEER_CODE);
        String elaboration = (String) dbObject.get("elaboration");
        
        Recipe recipe = new Recipe(beerCode, elaboration);
        
        BasicDBObject ingredients = (BasicDBObject) dbObject.get("ingredients");
        
        if(ingredients.containsField("malts"))
        {
            List<String> malts = (List<String>) ingredients.get("malts");
            recipe.malts.addAll(malts);
        }
        
        if(ingredients.containsField("hops"))
        {
            List<String> hops = (List<String>) ingredients.get("hops");
            recipe.hops.addAll(hops);
        }
        
        if(ingredients.containsField("yeasts"))
        {
            List<String> yeasts = (List<String>) ingredients.get("yeasts");
            recipe.yeasts.addAll(yeasts);
        }
        
        if(ingredients.containsField("fining_agents"))
        {
            List<String> finingAgents = (List<String>) ingredients.get("fining_agents");
            recipe.finingAgents.addAll(finingAgents);
        }
        
        return recipe;
        
    }
    

}