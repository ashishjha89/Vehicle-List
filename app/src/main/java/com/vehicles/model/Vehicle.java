package com.vehicles.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Vehicle implements Serializable {

    public String vehicleId;

    public String vrn;

    public String country;

    public String color;

    public String type;

    @JsonProperty("default")
    public boolean isDefault;

    public Vehicle() {

    }

    public Vehicle(String vehicleId, String vrn, String country, String color, String type, boolean isDefault) {
        this.vehicleId = vehicleId;
        this.vrn = vrn;
        this.country = country;
        this.color = color;
        this.type = type;
        this.isDefault = isDefault;
    }
}
