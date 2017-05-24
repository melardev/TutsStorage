package com.melardev.tutsstorage.model;

/**
 * Created by melardev on 5/21/2017.
 */

public class City {
    private String country;
    private String name;
    private double latitude;
    private double longitude;

    public City(String country, String name, double latitude, double longitude) {
        this.country = country;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String toJsonString() {
        return "{ \"country\": \"ES\",\n" +
                "    \"name\": \"Valencia\",\n" +
                "    \"latitude\": 39.466667,\n" +
                "    \"longitude\": -.366667}";
    }
}
