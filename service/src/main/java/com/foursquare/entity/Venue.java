package com.foursquare.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Venue {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private User user;

    private String fs_id;

    @ManyToMany
    @JoinTable(
            name = "venue_category",
            joinColumns = @JoinColumn(name = "venue_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    private Date added_at; //TO CHECK

    private String name;

    private String address;

    private String phone;

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

    public String getFs_id() {
        return fs_id;
    }

    public void setFs_id(String fs_id) {
        this.fs_id = fs_id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Date getAdded_at() {
        return added_at;
    }

    public void setAdded_at(Date added_at) {
        this.added_at = added_at;
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
                Objects.equal(fs_id, venue.fs_id) &&
                Objects.equal(categories, venue.categories) &&
                Objects.equal(added_at, venue.added_at) &&
                Objects.equal(name, venue.name) &&
                Objects.equal(address, venue.address) &&
                Objects.equal(phone, venue.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, user, fs_id, categories, added_at, name, address, phone);
    }
}