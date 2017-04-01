package org.arquillian.example.functional.utils;

import java.util.Collection;
import org.arquillian.example.functional.pages.Beer;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

public class BeersAssert extends GenericAssert<BeersAssert, Collection<Beer>> {

    private BeersAssert(Collection<Beer> actual) {
        super(BeersAssert.class, actual);
    }

    public static BeersAssert assertThat(Collection<Beer> beers) {
        return new BeersAssert(beers);
    }

    public BeersAssert shouldContain(Beer... beers) {
        Assertions.assertThat(actual).contains(beers);
        return this;
    }
}
