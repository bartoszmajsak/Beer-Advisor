package org.arquillian.example.service;

import static org.fest.assertions.Assertions.assertThat;

import javax.ejb.EJB;

import org.arquillian.example.domain.Beer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class BeersInserterTest
{

   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
                       .addPackage(Beer.class.getPackage())
                       .addClass(BeersInserter.class)
                       .addPackages(true, "org.fest")
                       .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                       .addAsManifestResource("test-persistence.xml", "persistence.xml");
   }

   @EJB
   BeersInserter beersInserter;

   @Test
   @ShouldMatchDataSet(value = "expected-beers.yml", orderBy = "name")
   @Cleanup(phase = TestExecutionPhase.AFTER)
   public void should_return_all_beert() throws Exception
   {
      assertThat(beersInserter).isNotNull();
   }

}