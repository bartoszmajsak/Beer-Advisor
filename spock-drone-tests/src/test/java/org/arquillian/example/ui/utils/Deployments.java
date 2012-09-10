package org.arquillian.example.ui.utils;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

public class Deployments
{
   public static WebArchive create()
   {
      final MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class)
                                                                     .loadMetadataFromPom("pom.xml")
                                                                     .goOffline();

      final Archive<?> webArchive = ShrinkWrap.createFromZipFile(WebArchive.class, resolver.artifacts("org.arquillian.example:beer-advisor-web:war:1.0.0-SNAPSHOT")
                                      .resolveAsFiles()[0]);

      return ShrinkWrap.create(WebArchive.class, "beer-advisor.war")
                       .merge(webArchive);
   }

}
