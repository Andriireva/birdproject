package com.ua.reva.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Bird model class
 */
@JsonIgnoreProperties
public class Bird {
    private String name;
    private String color;
    private String weight;
    private String height;

    public Bird() {
    }

    public Bird(String name, String color, String weight, String height) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
