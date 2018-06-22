package com.foursquare.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.foursquare.dao.SimilarVenuesDao;
import com.foursquare.dto.VenueDto;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.SimilarVenuesService;
import com.foursquare.validator.DaoResponseVenueValidatior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SimilarVenuesServiceImpl implements SimilarVenuesService {

    @Autowired
    private SimilarVenuesDao similarVenuesDao;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void emailSimilarVenues() {
        List<User> users = userRepository.findAll();

        SimpleMailMessage message = new SimpleMailMessage();

        if(!users.isEmpty()) {
            for (User user: users) {
                List<Venue> allVenuesOfUser = venueRepository.findAlldByUserId(user.getId());

                if(!allVenuesOfUser.isEmpty()) {
                    HashSet<VenueDto> similarVenuesDto = new HashSet<>();

                    for (Venue venue : allVenuesOfUser)
                    {
                       similarVenuesDto.addAll(mapFromJson(similarVenuesDao.getSimilarVenues(venue.getFsId())));
                    }

                    List<VenueDto> similarVenuesDtoFiltered = similarVenuesDto.stream()
                            .filter(venueDto -> allVenuesOfUser.stream()
                                    .noneMatch(v -> v.getFsId().equals(venueDto.getId())))
                            .collect(Collectors.toList());

                    message.setTo(user.getEmail());
                    message.setSubject("Similar venues. User: " + user.getId());
                    message.setText(similarVenuesDtoFiltered.toString());

                    mailSender.send(message);
                }
            }
        }
    }

    private List<VenueDto> mapFromJson(JsonNode jsonNode) {
        List<VenueDto> venues = null;

        Optional<JsonNode> venuesNode = Optional.ofNullable(jsonNode).map(venueNode -> venueNode.get("response")).
                map(responseNode -> responseNode.get("similarVenues")).
                map(responseNode -> responseNode.get("items"));


        if(venuesNode.isPresent())
        {
            venues = StreamSupport.stream(venuesNode.get().spliterator(), false)
                    .filter(DaoResponseVenueValidatior::isValidVenue)
                    .map(VenueDto::valueOf)
                    .collect(Collectors.toList());
        }

        return venues;
    }
}