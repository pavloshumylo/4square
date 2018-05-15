package com.foursquare.entity;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String email;
    private String city;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equal(name, user.name) &&
                Objects.equal(email, user.email) &&
                Objects.equal(city, user.city) &&
                Objects.equal(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, email, city, password);
    }
}