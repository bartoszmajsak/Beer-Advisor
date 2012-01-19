package org.arquillian.example.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.arquillian.example.domain.Beer;
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
   }

}
