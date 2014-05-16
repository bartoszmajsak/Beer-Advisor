package org.arquillian.example.repository.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBPort;
import com.mongodb.MongoClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.net.UnknownHostException;

@ApplicationScoped
public class MongoDBCollectionProducer
{

    MongoClient mongo;

    @PostConstruct
    public void init() throws UnknownHostException
    {
        mongo = new MongoClient("localhost", DBPort.PORT);
    }

    @Produces
    @RecipeCollection
    public DBCollection getRecipeCollection()
    {
        DB db = mongo.getDB("beers");
        return db.getCollection("recipes");
    }

}
