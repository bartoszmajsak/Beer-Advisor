package org.arquillian.example.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.BeerBuilder;
import org.arquillian.example.domain.Brewery;
import org.arquillian.example.domain.Country;
import org.arquillian.example.domain.Type;

@Singleton
@Startup
public class BeersInserter
{

   @PersistenceContext
   EntityManager em;

   @PostConstruct
   public void initializeBeerRepository()
   {
      Beer mocnyFull = BeerBuilder.create()
                                  .named("Mocny Full")
                                  .withPrice(BigDecimal.valueOf(1.0))
                                  .havingAlcohol(BigDecimal.valueOf(4.5))
                                  .from(new Brewery("Kiepski Browar", Country.POLAND))
                                  .ofType(Type.LAGER)
                                  .withCode("mocny_full")
                                  .build();
      save(mocnyFull);

      Brewery brewDog = new Brewery("Brew Dog", Country.SCOTLAND);
      Beer endOfHistory = BeerBuilder.create()
                                     .named("End of history")
                                     .withPrice(BigDecimal.valueOf(765.0))
                                     .havingAlcohol(BigDecimal.valueOf(55.0))
                                     .from(brewDog)
                                     .ofType(Type.BLOND_ALE)
                                     .withCode("end_of_history")
                                     .build();

      Beer bismarck = BeerBuilder.create()
                                 .named("Sink The Bismarck!")
                                 .withPrice(BigDecimal.valueOf(64.0))
                                 .havingAlcohol(BigDecimal.valueOf(41.0))
                                 .from(brewDog)
                                 .ofType(Type.QUADRUPEL_IPA)
                                 .withCode("bismarck")
                                 .build();

      save(endOfHistory);
      save(bismarck);

      Beer delirium = BeerBuilder.create()
                                 .named("Delirium Tremens")
                                 .withPrice(BigDecimal.valueOf(10.0))
                                 .havingAlcohol(BigDecimal.valueOf(8.5))
                                 .from(new Brewery("Brouwerij Huyghe", Country.BELGIUM))
                                 .ofType(Type.PALE_ALE)
                                 .withCode("delirium")
                                 .build();
      save(delirium);

      Beer kwak = BeerBuilder.create()
                             .named("Pauwel Kwak")
                             .withPrice(BigDecimal.valueOf(4.0))
                             .havingAlcohol(BigDecimal.valueOf(8.4))
                             .from(new Brewery("Brouwerij Bosteels", Country.BELGIUM))
                             .ofType(Type.AMBER)
                             .withCode("kwak")
                             .build();
      save(kwak);

      Beer bugel = BeerBuilder.create()
                              .named("Bügel")
                              .withPrice(BigDecimal.valueOf(3.0))
                              .havingAlcohol(BigDecimal.valueOf(4.8))
                              .from(new Brewery("Feldschlösschen", Country.SWITZERLAND))
                              .ofType(Type.VIENNA)
                              .withCode("bugel")
                              .build();
      save(bugel);

      Beer appenzellerSchwarzKrystall = BeerBuilder.create()
                                                   .named("Appenzeller Schwarzer Kristall")
                                                   .withPrice(BigDecimal.valueOf(4.0))
                                                   .havingAlcohol(BigDecimal.valueOf(6.3))
                                                   .from(new Brewery("Locher", Country.SWITZERLAND))
                                                   .ofType(Type.SCHWARZBIER)
                                                   .withCode("schwarzer_kristall")
                                                   .build();
      save(appenzellerSchwarzKrystall);

      Beer ipaNogne = BeerBuilder.create()
                                 .named("India Pale Ale")
                                 .withPrice(BigDecimal.valueOf(4.0))
                                 .havingAlcohol(BigDecimal.valueOf(7.5))
                                 .from(new Brewery("Nøgne ø", Country.NORWAY))
                                 .ofType(Type.IPA)
                                 .withCode("ipa_nogne")
                                 .build();
      save(ipaNogne);

      Beer darkForce = BeerBuilder.create()
                                  .named("Dark Force")
                                  .withPrice(BigDecimal.valueOf(4.0))
                                  .havingAlcohol(BigDecimal.valueOf(9.0))
                                  .from(new Brewery("Haand Bryggeriet", Country.NORWAY))
                                  .ofType(Type.RUSSIAN_IMPERIAL_STOUT)
                                  .withCode("dark_force")
                                  .build();

      save(darkForce);

   }

   private void save(Beer beer)
   {
      em.persist(beer);
   }

}
