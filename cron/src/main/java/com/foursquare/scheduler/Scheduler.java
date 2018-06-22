package com.foursquare.scheduler;

import com.foursquare.service.SimilarVenuesService;
import com.foursquare.service.TrendingCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private SimilarVenuesService similarVenuesService;
    @Autowired
    private TrendingCategoriesService trendingCategoriesService;

    @Scheduled(cron = "${cron.expression.weekly}")
    public void emailSimilarVenues() {
        similarVenuesService.emailSimilarVenues();
    }

    @Scheduled(cron = "${cron.expression.monthly}")
    public void emailTrendingCategories() {
        trendingCategoriesService.emailTrendingCategories();
    }
}