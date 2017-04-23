package com.vehicles.api;

import com.vehicles.VehiclesContract;

public interface ApiEndpoint {

    void getVehicleList(VehiclesContract.OnVehicleListCallback callback);
}