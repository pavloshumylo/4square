package com.foursquare.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    private String fs_id;

    @ManyToMany
    @JoinTable(
            name = "venue_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "venue_id")
    )
    private List<Venue> venues;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFs_id() {
        return fs_id;
    }

    public void setFs_id(String fs_id) {
        this.fs_id = fs_id;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                Objects.equal(fs_id, category.fs_id) &&
                Objects.equal(venues, category.venues);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, fs_id, venues);
    }
}