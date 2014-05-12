package org.arquillian.example.repository;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@ApplicationScoped
public class MongoDbFactory 
{

    MongoClient mongo;
    
    @Inject
    @ConfigProperty(name = "mongodb.host")
    String host;
    
    @Inject
    @ConfigProperty(name = "mongodb.port")
    Integer port;
    
    @PostConstruct
    public void init() throws UnknownHostException 
    {
        mongo = new MongoClient(host, port);
    }
    
    @Produces
    @RecipeCollection
    public DBCollection getRecipeCollection() 
    {
        DB db = mongo.getDB("test");
        return db.getCollection("recipes");
    }
    
}
