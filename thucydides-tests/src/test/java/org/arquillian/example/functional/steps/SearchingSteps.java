package org.arquillian.example.functional.steps;

import java.util.List;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.arquillian.example.functional.pages.Beer;
import org.arquillian.example.functional.pages.BeerAdvisorPage;
import org.arquillian.example.functional.utils.BeersAssert;

public class SearchingSteps extends ScenarioSteps {

    private static final long serialVersionUID = 6793275010489146537L;

    public SearchingSteps(Pages pages) {
        super(pages);
    }

    @Step
    public void on_main_beer_advisor_page() {
        onBeerAdvisorMainPage().open();
    }

    @Step
    public void search_for(String searchCriteria) {
        onBeerAdvisorMainPage().searchFor(searchCriteria);
    }

    @Step
    public void should_contain_beers(Beer... beers) {
        final List<Beer> listedBeers = onBeerAdvisorMainPage().getListedBeers();
        BeersAssert.assertThat(listedBeers).shouldContain(beers);
    }

    private BeerAdvisorPage onBeerAdvisorMainPage() {
        return getPages().currentPageAt(BeerAdvisorPage.class);
    }
}
