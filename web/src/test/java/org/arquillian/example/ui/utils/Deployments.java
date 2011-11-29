package org.arquillian.example.ui.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.arquillian.example.controller.BeerAdvisorController;
import org.arquillian.example.domain.Beer;
import org.arquillian.example.repository.BeerRepository;
import org.arquillian.example.service.BeerService;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;


public class Deployments
{
   private static final String WEBAPP_SRC = "src/main/webapp";

   public static WebArchive create()
   {
      final MavenDependencyResolver mvnResolver = DependencyResolvers.use(MavenDependencyResolver.class)
                                                                     .loadMetadataFromPom("pom.xml")
                                                                     .goOffline();

      WebArchive warArchive = ShrinkWrap.create(WebArchive.class, "beer-advisor.war");
      addWebResources(warArchive);
      return warArchive.addPackages(true, Beer.class.getPackage(),
                                          BeerRepository.class.getPackage(),
                                          BeerService.class.getPackage(),
                                          BeerAdvisorController.class.getPackage())
                       .addAsWebInfResource("test-persistence.xml", ArchivePaths.create("classes/META-INF/persistence.xml"))
                       .addAsLibraries(mvnResolver.artifact("com.google.guava:guava:r09").resolveAsFiles());
   }

   private static void addWebResources(WebArchive warArchive)
   {
      final File webAppDirectory = new File(WEBAPP_SRC);
      for (File file : FileUtils.listFiles(webAppDirectory, null, true))
      {
         if (!file.isDirectory())
         {
            warArchive.addAsWebResource(file, file.getPath().substring(WEBAPP_SRC.length()));
         }
      }

   }

}
