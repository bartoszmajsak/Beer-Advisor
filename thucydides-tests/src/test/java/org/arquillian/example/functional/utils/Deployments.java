package org.arquillian.example.functional.utils;

import java.io.File;

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

      final File webArchive = resolver.artifacts("org.arquillian.example:beer-advisor-web:war:1.0.0-SNAPSHOT")
                                      .resolveAsFiles()[0];

      return ShrinkWrap.createFromZipFile(WebArchive.class, webArchive);
   }

}
