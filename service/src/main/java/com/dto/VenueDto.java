package com.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class VenueDto {

    private String id;
    private String name;
    private String phone;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public VenueDto jsonMapping(String json) throws IOException {
        JsonNode venueDtoNode = new ObjectMapper().readTree(json);
        VenueDto venue = new VenueDto();
        venue.setId(venueDtoNode.get("response").get("venues").get(0).get("id").textValue());
        venue.setName(venueDtoNode.get("response").get("venues").get(0).get("name").textValue());
        venue.setAddress(venueDtoNode.get("response").get("venues").get(0).get("location").get("address").textValue());
        venue.setPhone(venueDtoNode.get("response").get("venues").get(0).get("contact").get("phone").textValue());
        return venue;
    }
}
