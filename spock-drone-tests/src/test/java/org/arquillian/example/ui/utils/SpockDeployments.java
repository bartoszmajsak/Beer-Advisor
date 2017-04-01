package org.arquillian.example.ui.utils;

import java.io.File;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class SpockDeployments {
    public static WebArchive create() {
        final File beerAdvisor = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("org.arquillian.example:beer-advisor-web:war:1.0.0-SNAPSHOT")
            .withoutTransitivity()
            .asSingleFile();

        final Archive<?> webArchive = ShrinkWrap.createFromZipFile(WebArchive.class, beerAdvisor);

        return ShrinkWrap.create(WebArchive.class, "beer-advisor-spock.war")
            .merge(webArchive);
    }
}
