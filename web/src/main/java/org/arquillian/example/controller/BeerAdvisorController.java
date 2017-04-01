package org.arquillian.example.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.arquillian.example.domain.Beer;
import org.arquillian.example.repository.BeerCriteria;
import org.arquillian.example.service.BeerService;

@Named
@RequestScoped
public class BeerAdvisorController {

    private static final String NONE = "none";

    private boolean notFound;

    private List<Beer> beers = Collections.emptyList();

    @Inject
    private BeerService beerService;

    private String criteria;

    public void loadBeers(AjaxBehaviorEvent event) {
        if (criteria == null || criteria.trim().isEmpty()) {
            criteria = NONE;
        }

        final Collection<Beer> result = beerService.fetchByCriteria(criteria);
        notFound = result.isEmpty() && !NONE.equals(criteria);
        beers = Lists.newArrayList(result);
    }

    public List<String> getAvailableCriteria() {
        return Lists.transform(BeerCriteria.allCriteria(), new Function<BeerCriteria, String>() {
            @Override
            public String apply(BeerCriteria criterion) {
                return criterion.getCriteriaString();
            }
        });
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String filter) {
        criteria = filter;
    }

    public boolean isNotFound() {
        return notFound;
    }

    public void setNotFound(boolean notFound) {
        this.notFound = notFound;
    }
}
