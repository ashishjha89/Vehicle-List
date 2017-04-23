package com.vehicles;

import com.vehicles.model.VehicleList;

public interface VehiclesContract {

    interface VehiclesListView {
        void showProgress(boolean show);

        void setVehicleList(VehicleList vehiclesList);

        void onVehicleClick(int position);
    }

    interface OnVehicleListCallback {
        void onVehicleListLoaded(VehicleList vehiclesList);
    }

    void detachView();

    void getVehicleList();
}
