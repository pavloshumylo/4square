package com.foursquare.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class Venue {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private User user;

    @Column(name = "fs_id")
    @NotNull(message = "Foursquare id shouldn't be null.")
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
}