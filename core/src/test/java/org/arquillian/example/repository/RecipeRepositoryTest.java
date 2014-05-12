package org.arquillian.example.repository;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;
import org.arquillian.example.util.MongoDBConfigurationFile;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RecipeRepositoryTest {

    @Deployment
    public static JavaArchive createDeployment() 
    {
        
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
                                            .addAsServiceProvider(PropertyFileConfig.class, MongoDBConfigurationFile.class)
                                            .addClasses(MongoDBConfigurationFile.class, MongoDbFactory.class, MongoDBRecipeRepository.class, RecipeCollection.class)
                                            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        JavaArchive[] deltaSpikeLibrary = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.core:deltaspike-core-impl").withTransitivity().as(JavaArchive.class);
        
        for (JavaArchive deltaSpikeDependecy : deltaSpikeLibrary) 
        {
            javaArchive.merge(deltaSpikeDependecy);
        }
        
        return javaArchive;
        
    }
    
}
