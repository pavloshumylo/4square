package com.foursquare.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.foursquare.dao.SearchDao;
import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.exception.VenueException;
import com.foursquare.repository.CategoryRepository;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.VenueService;
import com.foursquare.validator.DaoResponseVenueValidatior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SearchDao searchDao;

    public void save(Venue venue) {
        User user = getCurrentUser();

        if(venueRepository.findByUserIdAndFsId(user.getId(), venue.getFsId()) == null) {
            venue.setUser(user);
            venue.setAddedAt(new Date());
            fillInVenueByDataFromApi(venue);
            venueRepository.save(venue);
        } else {
            throw new VenueException("No update allowed. Venue already exist with fs id: " + venue.getFsId());
        }
    }

    public void remove(Venue venue) {
        User user = getCurrentUser();
        Venue venueFound = venueRepository.findByUserIdAndFsId(user.getId(), venue.getFsId());

        if(venueFound != null) {
            venueRepository.delete(venueFound.getId());
        } else {
            throw new VenueException("No delete allowed. Venue doesn't exist with fs id: " + venue.getFsId());
        }
    }

    private User getCurrentUser() {
        return userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private void fillInVenueByDataFromApi(Venue venue) {
        Optional<JsonNode> venueNode = Optional.ofNullable(searchDao.search(venue.getFsId())).
                map(venueNodeOptional -> venueNodeOptional.get("response")).map(responseNode -> responseNode.get("venue"));

        venueNode.ifPresent(venueNodeOptional -> {
            if(DaoResponseVenueValidatior.isValidVenue(venueNodeOptional)) {

                if(venueNodeOptional.get("name") != null) {
                    venue.setName(venueNodeOptional.get("name").textValue());
                }

                Optional.ofNullable(venueNodeOptional.get("contact")).
                    map(contactNode -> contactNode.get("phone")).ifPresent(phoneNode -> venue.setPhone(phoneNode.textValue()));

                Optional.ofNullable(venueNodeOptional.get("location")).
                    map(locationNode -> locationNode.get("address")).ifPresent(addresssNode ->
                    venue.setAddress(addresssNode.textValue()));

                Optional.ofNullable(venueNodeOptional.get("categories")).ifPresent(
                        categoriesNode -> categoriesNode.forEach(categoryNode -> {

                            if(categoryNode.get("id") != null){
                                Category category = categoryRepository.findByFsId(categoryNode.get("id").textValue());

                                if (category == null) {
                                    category = new Category();
                                    category.setFsId(categoryNode.get("id").textValue());

                                    if(categoryNode.get("name") != null) {
                                        category.setName(categoryNode.get("name").textValue());
                                    }
                                    categoryRepository.save(category);
                                    }
                                    venue.getCategories().add(category);
                                    } else {
                                        throw new VenueException("Category foursquare id doesn't exist");
                                    }
                        }));
            } else {
                throw new VenueException("Cannot validate venue.");
            }
        });
    }
}