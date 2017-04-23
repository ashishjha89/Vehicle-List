package com.vehicles.api;

import com.vehicles.model.VehicleList;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiService {

    @GET("vehicles")
    Call<VehicleList> getVehicleList();
}
