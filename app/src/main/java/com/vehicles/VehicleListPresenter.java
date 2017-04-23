package com.vehicles;

import com.vehicles.api.ApiEndpoint;
import com.vehicles.model.VehicleList;

class VehicleListPresenter implements VehiclesContract {

    private VehiclesListView view;
    private ApiEndpoint apiEndpoint;

    VehicleListPresenter(VehiclesListView view, ApiEndpoint apiEndpoint) {
        this.view = view;
        this.apiEndpoint = apiEndpoint;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getVehicleList() {
        view.showProgress(true);
        apiEndpoint.getVehicleList(new OnVehicleListCallback() {
            @Override
            public void onVehicleListLoaded(VehicleList vehiclesList) {
                if (view != null) {
                    view.showProgress(false);
                    view.setVehicleList(vehiclesList);
                }
            }
        });
    }
}
