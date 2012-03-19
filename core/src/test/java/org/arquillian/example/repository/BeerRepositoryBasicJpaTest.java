package org.arquillian.example.repository;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Brewery;
import org.arquillian.example.domain.Country;
import org.arquillian.example.domain.Type;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * The most simple way of writing JPA tests.
 *
 */
@RunWith(Arquillian.class)
public class BeerRepositoryBasicJpaTest
{

   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
                       .addPackage(Beer.class.getPackage())
                       .addPackage(BeerRepository.class.getPackage())
                       .addPackages(true, "org.fest")
                       .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                       .addAsManifestResource("test-persistence.xml", "persistence.xml");
   }

   @Inject
   BeerRepository beerRepository;

   @PersistenceContext
   EntityManager em;

   @Inject
   UserTransaction utx;

   @Before
   public void preparePersistenceTest() throws Exception {
       clearDatabase();
       insertData();
       startTransaction();
   }

   @After
   public void commitTransaction() throws Exception {
       utx.commit();
   }

   private void clearDatabase() throws Exception {
       utx.begin();
       em.joinTransaction();
       em.createQuery("delete from Beer").executeUpdate();
       utx.commit();
   }

   private void insertData() throws Exception {
       utx.begin();
       em.joinTransaction();
       Beer mocnyFull = new Beer(new Brewery("Kiepski Browar", Country.POLAND),
             Type.LAGER, "Mocny Full",
             BigDecimal.valueOf(1.0), BigDecimal.valueOf(4.5));
       mocnyFull.setCode("mocny_full");
       em.persist(mocnyFull);

       Brewery brewDog = new Brewery("Brew Dog", Country.SCOTLAND);
       Beer endOfHistory = new Beer(brewDog, Type.BLOND_ALE, "End of history", BigDecimal.valueOf(765.0), BigDecimal.valueOf(55.0));
       endOfHistory.setCode("end_of_history");
       brewDog.addBeer(endOfHistory);
       Beer bismarck = new Beer(brewDog, Type.QUADRUPEL_IPA, "Sink The Bismarck!", BigDecimal.valueOf(64.0), BigDecimal.valueOf(41.0));
       bismarck.setCode("bismarck");

       brewDog.addBeer(bismarck);
       em.persist(endOfHistory);
       em.persist(bismarck);
       em.persist(brewDog);

       Beer delirium = new Beer(new Brewery("Brouwerij Huyghe", Country.BELGIUM),
             Type.PALE_ALE, "Delirium Tremens",
             BigDecimal.valueOf(10.0), BigDecimal.valueOf(8.5));
       delirium.setCode("delirium");

       em.persist(delirium);

       Beer kwak = new Beer(new Brewery("Brouwerij Bosteels", Country.BELGIUM),
             Type.AMBER, "Pauwel Kwak",
             BigDecimal.valueOf(4.0), BigDecimal.valueOf(8.4));
       kwak.setCode("kwak");
       em.persist(kwak);
       utx.commit();
   }

   private void startTransaction() throws Exception {
       utx.begin();
   }

   @Test
   public void shouldFindStrongestBeer() throws Exception
   {
      // given
      String expectedName = "End of history";
      BigDecimal expectedVoltage = BigDecimal.valueOf(55.0);

      // when
      Set<Beer> beers = beerRepository.strongest();
      Beer firstBeer = beers.iterator().next();

      // then
      assertThat(firstBeer.getName()).isEqualTo(expectedName);
      assertThat(firstBeer.getAlcohol()).isEqualByComparingTo(expectedVoltage);
   }

}
