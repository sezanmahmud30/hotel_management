package com.sezanmahmud.hotel_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class Location {
    @Id
    private int id;

    private String Name;
    private String image;

    public Location() {
    }

    public Location(int id, String name, String image) {
        this.id = id;
        Name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
