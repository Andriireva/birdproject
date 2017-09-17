package com.ua.reva.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Sighting of the bird
 */
@JsonIgnoreProperties
public class Sighting {
    private String location;
    private Date dateTime;
    private String birdName;

    public Sighting(String location, Date dateTime) {
        this.location = location;
        this.dateTime = dateTime;
    }

    public Sighting(String birdName, String location, Date dateTime) {
        this.location = location;
        this.dateTime = dateTime;
        this.birdName = birdName;
    }

    public Sighting() {
    }

    public String getBirdName() {
        return birdName;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
