package com.vehicles;

import com.vehicles.api.ApiEndpoint;
import com.vehicles.model.VehicleList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VehicleListPresenterTest {

    @Mock
    private VehiclesContract.VehiclesListView view;

    @Mock
    private ApiEndpoint apiEndpoint;

    @Mock
    private VehicleList vehicleList;

    @Captor
    private ArgumentCaptor<VehiclesContract.OnVehicleListCallback> onVehicleListCallbackCaptor;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getVehicleListTest() {
        VehicleListPresenter presenter = new VehicleListPresenter(view, apiEndpoint);

        // Call method
        presenter.getVehicleList();

        // Verify the progress bar is shown
        verify(view).showProgress(true);
        verify(apiEndpoint).getVehicleList(onVehicleListCallbackCaptor.capture());
        onVehicleListCallbackCaptor.getValue().onVehicleListLoaded(vehicleList);

        // Then progress indicator is hidden and vehicle list is shown in UI
        verify(view).showProgress(false);
        verify(view).setVehicleList(vehicleList);
    }
}
