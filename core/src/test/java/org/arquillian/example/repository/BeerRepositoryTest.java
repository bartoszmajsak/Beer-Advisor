package org.arquillian.example.repository;

import java.math.BigDecimal;
import java.util.Set;
import javax.inject.Inject;
import org.arquillian.example.domain.Beer;
import org.arquillian.example.domain.Country;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
public class BeerRepositoryTest {

    @Inject
    BeerRepository beerRepository;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addPackage(Beer.class.getPackage())
            .addPackage(BeerRepository.class.getPackage())
            .addPackages(true, "org.fest")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("test-persistence.xml", "persistence.xml");
    }

    @Test
    @UsingDataSet("beers.yml")
    public void should_find_strongest_beer() throws Exception {
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

    @Test
    @UsingDataSet("beers.yml")
    public void should_find_cheapest_beer() throws Exception {
        // given
        String expectedName = "Mocny Full";
        BigDecimal expectedPrice = BigDecimal.valueOf(1.0);

        // when
        Set<Beer> beers = beerRepository.cheapest();
        Beer firstBeer = beers.iterator().next();

        // then
        assertThat(firstBeer.getName()).isEqualTo(expectedName);
        assertThat(firstBeer.getPrice()).isEqualByComparingTo(expectedPrice);
    }

    @Test
    @UsingDataSet("beers.yml")
    public void should_find_all_belgian_beers() throws Exception {
        // given
        Country belgium = Country.BELGIUM;

        // when
        Set<Beer> beers = beerRepository.from(belgium);

        // then
        assertThat(beers).hasSize(2);
    }

    @Test
    @UsingDataSet("beers.yml")
    public void should_find_all_polish_beers() throws Exception {
        // given
        Country poland = Country.POLAND;

        // when
        Set<Beer> beers = beerRepository.from(poland);

        // then
        assertThat(beers).hasSize(1);
    }

    @Test
    @UsingDataSet("beers.yml")
    public void should_return_all_beers() throws Exception {
        // given
        int expectedAmountOfBeers = 7;

        // when
        Set<Beer> allBeers = beerRepository.fetchAll();

        // then
        assertThat(allBeers).hasSize(expectedAmountOfBeers);
    }

    @Test
    @UsingDataSet("beers.yml")
    public void should_return_beer_by_its_id() throws Exception {
        // given
        Long beerId = 1L;
        String expectedName = "Mocny Full";

        // when
        Beer beers = beerRepository.getById(beerId);

        // then
        assertThat(beers.getName()).isEqualTo(expectedName);
    }

    @Test
    @UsingDataSet("beers.yml")
    @ShouldMatchDataSet("beers-without-mocny-full.yml")
    public void should_remove_beer_by_id_and_its_unique_brewery() throws Exception {
        // given
        Long beerId = 1L;

        // when
        beerRepository.delete(beerId);

        // then
        // verified by @Should
    }

    @Test
    @UsingDataSet("beers.yml")
    @ShouldMatchDataSet("beers-without-bismarck.yml")
    public void should_remove_beer_but_not_brewery() throws Exception {
        // given
        Beer bismarck = beerRepository.getById(3L);

        // when
        beerRepository.delete(bismarck);

        // then
        // verified by @Should
    }
}
