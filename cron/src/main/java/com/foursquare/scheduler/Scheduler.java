package com.foursquare.scheduler;

import com.foursquare.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Scheduled(cron = "${cron.expression.weekly}")
    public void emailSimilarVenues() {
        emailNotificationService.emailSimilarVenues();
    }

    @Scheduled(cron = "${cron.expression.monthly}")
    public void emailTrendingCategories() {
        emailNotificationService.emailTrendingCategories();
    }
}