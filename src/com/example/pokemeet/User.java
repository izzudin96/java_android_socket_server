package com.example.pokemeet;

import java.io.Serializable;

public class User implements Serializable {
    private final int id;
    private final String email;
    private String latitude = "0";
    private static int count = 0;
    private String longitude = "0";
    private static final long serialVersionUID = 2147483649L;

    public User(String email) {
        count++;
        this.id = count;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getId() {
        return this.id;
    }

    public void updateLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    @Override
    public String toString() {
        return "User ID: " + getId() + " || Email: " + getEmail() + " || Latitude: " + getLatitude() + " || Longitude: " + getLongitude();
    }
}