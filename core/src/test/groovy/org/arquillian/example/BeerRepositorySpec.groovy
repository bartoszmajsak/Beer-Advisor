package org.arquillian.example

import org.arquillian.example.domain.Beer
import org.arquillian.example.repository.BeerRepository
import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.persistence.ShouldMatchDataSet
import org.jboss.arquillian.persistence.UsingDataSet
import org.jboss.arquillian.spock.ArquillianSputnik
import org.jboss.shrinkwrap.api.Archive
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.asset.EmptyAsset
import org.jboss.shrinkwrap.api.spec.JavaArchive
import org.junit.runner.RunWith
import spock.lang.Specification
import spock.lang.Stepwise

import javax.inject.Inject

import static org.fest.assertions.Assertions.assertThat

@Stepwise
@RunWith(ArquillianSputnik)
class BeerRepositorySpecification extends Specification
{

   @Deployment
   public static Archive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addPackage(Beer.class.getPackage())
            .addPackage(BeerRepository.class.getPackage())
            .addPackages(true, "org.fest")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("test-persistence.xml", "persistence.xml");
   }

   @Inject
   BeerRepository beerRepository

   @UsingDataSet("beers.yml")
   def "should find the strongest beer"()
   {
      given:
         String expectedName = "End of history"
         BigDecimal expectedVoltage = BigDecimal.valueOf(55.0)

      when:
         Set<Beer> beers = beerRepository.strongest()
         Beer firstBeer = beers.iterator().next()

      then:
         assertThat(firstBeer.getName()).isEqualTo(expectedName)
         assertThat(firstBeer.getAlcohol()).isEqualByComparingTo(expectedVoltage)
   }

   @UsingDataSet("beers.yml")
   @ShouldMatchDataSet("beers-without-mocny-full.yml")
   def "should remove beer by id and its unique brewery"()
   {
      given:
         Long beerId = 1L;

      when:
         beerRepository.delete(beerId);

      then:
         assertThat(beerRepository.findById(beerId)).isNull()
   }

}
