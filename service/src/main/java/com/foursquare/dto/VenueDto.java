package com.foursquare.dto;

import com.google.common.base.Objects;

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
}