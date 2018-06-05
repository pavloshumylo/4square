package com.foursquare.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.foursquare.dao.SearchDao;
import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.exception.ResourceNotFoundException;
import com.foursquare.exception.BadRequestException;
import com.foursquare.repository.CategoryRepository;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.VenueService;
import com.foursquare.validator.DaoResponseVenueValidatior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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

    @Transactional
    public void save(String fsId) {
        if(venueRepository.findByUserIdAndFsId(getCurrentUser().getId(), fsId) == null) {
            venueRepository.save(fillInVenueByDataFromApi(fsId));
        } else {
            throw new BadRequestException("Unable to create venue with id: " + fsId + " Venue exists already.");
        }
    }

    @Transactional
    public void remove(String fsId) {
        User user = getCurrentUser();
        Venue venueFound = venueRepository.findByUserIdAndFsId(user.getId(), fsId);

        if(venueFound != null) {
            venueRepository.delete(venueFound.getId());
        } else {
            throw new ResourceNotFoundException("Resource with id: " + fsId + " not found");
        }
    }

    public List<Venue> getAll() {
        User user = getCurrentUser();
        List<Venue> venues = venueRepository.findAlldByUserId(user.getId());

        return venues;
    }

    private User getCurrentUser() {
        return userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional
    private Venue fillInVenueByDataFromApi(String fsId) {
        Optional<JsonNode> venueNode = Optional.ofNullable(searchDao.search(fsId)).
                map(venueNodeOptional -> venueNodeOptional.get("response")).map(responseNode -> responseNode.get("venue"));

        if(venueNode.isPresent()) {
            JsonNode venueJsonNode = venueNode.get();

            if(DaoResponseVenueValidatior.isValidVenue(venueJsonNode)) {
                Venue venue = Venue.valueOf(venueJsonNode);
                venue.setAddedAt(new Date());
                venue.setUser(getCurrentUser());

                Optional.ofNullable(venueJsonNode.get("categories")).ifPresent(
                    categoriesNode -> categoriesNode.forEach(categoryNode -> {
                        Category category = categoryRepository.findByFsId(categoryNode.get("id").textValue());

                                if (category == null) {
                                    category = Category.valueOf(categoryNode);
                                    categoryRepository.save(category);
                                }
                                venue.getCategories().add(category);
                    }));
                return venue;
            } else {
                throw new BadRequestException("Invalid venue.");
            }
        } else {
            throw new BadRequestException("JsonNode from api doesn't have venue.");
        }
    }
}