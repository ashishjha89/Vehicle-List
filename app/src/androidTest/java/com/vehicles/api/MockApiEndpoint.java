package com.vehicles.api;

import com.vehicles.VehiclesContract;
import com.vehicles.model.Vehicle;
import com.vehicles.model.VehicleList;

import java.util.ArrayList;

public class MockApiEndpoint implements ApiEndpoint {

    public static Vehicle VEHICLE1 = new Vehicle("id1", "vrn1", "country1", "color1", "type1", true);

    public static Vehicle VEHICLE2 = new Vehicle("id2", "vrn2", "country2", "color2", "type2", false);

    @Override
    public void getVehicleList(VehiclesContract.OnVehicleListCallback callback) {
        VehicleList vehicleList = new VehicleList();
        vehicleList.vehicles = new ArrayList<>();
        vehicleList.vehicles.add(VEHICLE1);
        vehicleList.vehicles.add(VEHICLE2);
        callback.onVehicleListLoaded(vehicleList);
    }
}
