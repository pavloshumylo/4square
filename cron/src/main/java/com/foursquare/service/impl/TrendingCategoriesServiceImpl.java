package com.foursquare.service.impl;

import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.TrendingCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrendingCategoriesServiceImpl implements TrendingCategoriesService {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void emailTrendingCategories() {

        LocalDateTime cronInvokationDateTime = LocalDateTime.now();
        int monthNumber = cronInvokationDateTime.getMonthValue() - 1;
        int yearNumber = cronInvokationDateTime.getYear();

        if(monthNumber == 0) {
            monthNumber = 12;
            --yearNumber;
        }

        LocalDateTime previousMonthDateTime = LocalDateTime.of(yearNumber, monthNumber, cronInvokationDateTime.getDayOfMonth(),
                cronInvokationDateTime.getHour(), cronInvokationDateTime.getMinute(), cronInvokationDateTime.getSecond());

        List<Venue> allMonthlyVenues = venueRepository.findAllByAddedAtGreaterThanEqual(Date.from(previousMonthDateTime.atZone(ZoneId.systemDefault()).toInstant()));

        if (!allMonthlyVenues.isEmpty()) {

            HashMap<Category, Integer> monthlyCategoriesOfVenues = new HashMap<>();

            for (Venue venue : allMonthlyVenues) {
                for (Category category : venue.getCategories()) {
                    if (!monthlyCategoriesOfVenues.containsKey(category)) {
                        monthlyCategoriesOfVenues.put(category, 0);
                    }
                }
            }

            allMonthlyVenues.stream().forEach(venue ->
                    venue.getCategories().stream().forEach(category ->
                            monthlyCategoriesOfVenues.put(category, monthlyCategoriesOfVenues.get(category) + 1)));

            List<Map.Entry<Category, Integer>> sortedMonthlyCategoriesByValue = monthlyCategoriesOfVenues.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            mailing(getTrendingCategoriesFromSortedList(sortedMonthlyCategoriesByValue));
        }
    }

    private List<Map.Entry<Category, Integer>> getTrendingCategoriesFromSortedList(List<Map.Entry<Category, Integer>> sortedCategoriesList) {
        List<Map.Entry<Category, Integer>> trendingCategories = new ArrayList<>();
        int indexValue = 0;
        int trendingCategoriesQuantity = 3;

        if (sortedCategoriesList.size() < 3) {
            trendingCategoriesQuantity = 1;
        }

        for(int i = 0; i < trendingCategoriesQuantity; i++) {

            Map.Entry<Category, Integer> entryByIndex = sortedCategoriesList.get(indexValue);
            trendingCategories.add(entryByIndex);

            for (int a = indexValue + 1; a < sortedCategoriesList.size(); a++) {
                ++indexValue;
                if (entryByIndex.getValue() == sortedCategoriesList.get(a).getValue()) {
                    trendingCategories.add(sortedCategoriesList.get(a));
                } else {
                    break;
                }
            }
        }

        return trendingCategories;
    }

    private void mailing(List<Map.Entry<Category, Integer>> trendingCategories) {
        SimpleMailMessage message = new SimpleMailMessage();

        List<User> users = userRepository.findAll();

        for(User user : users) {
            message.setTo(user.getEmail());
            message.setSubject("Trending monthly categories");
            List<String> messagesToEmail = new ArrayList<>();
            for (Map.Entry<Category, Integer> category : trendingCategories) {
                messagesToEmail.add("Category: " + category.getKey().getName() + "; Quantity: " + category.getValue());
            }
            message.setText(messagesToEmail.toString());
            mailSender.send(message);
        }
    }
}