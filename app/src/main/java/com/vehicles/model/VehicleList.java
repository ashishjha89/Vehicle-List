package com.vehicles.model;

import java.io.Serializable;
import java.util.List;

public class VehicleList implements Serializable {

    public int count;

    public List<Vehicle> vehicles;

    public int currentPage;

    public int nextPage;

    public int totalPages;
}
