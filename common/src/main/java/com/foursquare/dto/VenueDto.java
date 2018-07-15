package com.foursquare.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.foursquare.entity.Venue;
import com.google.common.base.Objects;

import java.util.Optional;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VenueDto venueDto = (VenueDto) o;
        return Objects.equal(id, venueDto.id) &&
                Objects.equal(name, venueDto.name) &&
                Objects.equal(phone, venueDto.phone) &&
                Objects.equal(address, venueDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, phone, address);
    }

    public static VenueDto valueOf(Venue venueFromNode) {
        VenueDto venueDto = new VenueDto();

        venueDto.setId(venueFromNode.getFsId());
        venueDto.setName(venueFromNode.getName());
        venueDto.setPhone(venueFromNode.getPhone());
        venueDto.setAddress(venueFromNode.getAddress());

        return venueDto;
    }

    public static VenueDto valueOf(JsonNode node) {
        VenueDto venueDto = new VenueDto();
        if(node.get("id") != null) {
            venueDto.setId(node.get("id").textValue());
        }

        if(node.get("name") != null) {
            venueDto.setName(node.get("name").textValue());
        }

        Optional.ofNullable(node.get("contact")).
                map(contactNode -> contactNode.get("phone")).ifPresent(phoneNode -> venueDto.setPhone(phoneNode.textValue()));

        Optional.ofNullable(node.get("location")).
                map(locationNode -> locationNode.get("address")).ifPresent(addresssNode ->
                venueDto.setAddress(addresssNode.textValue()));

        return venueDto;
    }
}