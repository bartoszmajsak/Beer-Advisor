package org.arquillian.example.ui.utils;

import java.util.Collection;
import org.arquillian.example.ui.web.Beer;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

public class BeersAssert extends GenericAssert<BeersAssert, Collection<BeersAssert>> {

    private BeersAssert(Collection<BeersAssert> actual) {
        super(BeersAssert.class, actual);
    }

    public static BeersAssert assertThat(Collection<BeersAssert> beers) {
        return new BeersAssert(beers);
    }

    public BeersAssert shouldContain(Beer... beers) {
        Assertions.assertThat(actual).contains(beers);
        return this;
    }
}
