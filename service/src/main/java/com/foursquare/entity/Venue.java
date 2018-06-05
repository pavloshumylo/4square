package com.foursquare.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
public class Venue {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private User user;

    @Column(name = "fs_id")
    private String fsId;

    @ManyToMany
    @JoinTable(
            name = "venue_category",
            joinColumns = @JoinColumn(name = "venue_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @Column(name = "added_at")
    private Date addedAt;

    private String name;

    private String address;

    private String phone;

    public Venue() {
        this.categories = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String fsId) {
        this.fsId = fsId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return id == venue.id &&
                Objects.equal(user, venue.user) &&
                Objects.equal(fsId, venue.fsId) &&
                Objects.equal(categories, venue.categories) &&
                Objects.equal(addedAt, venue.addedAt) &&
                Objects.equal(name, venue.name) &&
                Objects.equal(address, venue.address) &&
                Objects.equal(phone, venue.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, user, fsId, categories, addedAt, name, address, phone);
    }

    public static Venue valueOf(JsonNode node) {
        Venue venue = new Venue();
        if(node.get("id") != null) {
            venue.setFsId(node.get("id").textValue());
        }

        if(node.get("name") != null) {
            venue.setName(node.get("name").textValue());
        }

        Optional.ofNullable(node.get("contact")).
                map(contactNode -> contactNode.get("phone")).ifPresent(phoneNode -> venue.setPhone(phoneNode.textValue()));

        Optional.ofNullable(node.get("location")).
                map(locationNode -> locationNode.get("address")).ifPresent(addresssNode ->
                venue.setAddress(addresssNode.textValue()));

        return venue;
    }
}