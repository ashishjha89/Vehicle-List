package com.vehicles;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.vehicles.model.VehicleList;
import com.vehicles.util.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppCompatActivity.class, ButterKnife.class, VehicleListActivity.VehicleListAdapter.class,
        Utils.class})
public class VehicleListActivityTest {

    private VehicleListActivity activity;

    private VehicleListActivity spy;

    @Mock
    private Intent intent;

    @Mock
    private Bundle bundle;

    @Mock
    private VehicleList vehicleList;

    @Mock
    private RecyclerView recyclerView;

    @Mock
    private View noInternetView;

    @Mock
    private VehicleListPresenter presenter;

    @Mock
    private Unbinder unbinder;

    @Mock
    private VehicleListActivity.VehicleListAdapter adapter;

    @Mock
    private ProgressBar progressBar;

    @Mock
    private View somethingWentWrong;

    @Mock
    private IntentFilter intentFilter;

    @Before
    public void setup() {
        Method[] appCompatActivityMethods = PowerMockito.methods(AppCompatActivity.class,
                "onCreate", "onSaveInstanceState", "onDestroy", "onResume", "onPause");
        PowerMockito.suppress(appCompatActivityMethods);

        when(intent.getExtras()).thenReturn(bundle);
        when(intent.getSerializableExtra("VEHICLE_ARGS")).thenReturn(vehicleList);

        activity = new VehicleListActivity();
        spy = Mockito.spy(activity);

        when(spy.getRecyclerView()).thenReturn(recyclerView);
        when(spy.getPresenter()).thenReturn(presenter);

        PowerMockito.mockStatic(ButterKnife.class);
        when(ButterKnife.bind(spy)).thenReturn(unbinder);

        Mockito.doReturn(intent).when(spy).getIntent();
        Mockito.doNothing().when(spy).setContentView(anyInt());

        when(spy.getAdapter()).thenReturn(adapter);
        when(spy.getProgressBar()).thenReturn(progressBar);
        when(spy.getNoInternetView()).thenReturn(noInternetView);
        when(spy.getSomethingWentWrong()).thenReturn(somethingWentWrong);
        Mockito.doReturn(intentFilter).when(spy).getNetworkStateIntentFilter();
        Mockito.doReturn(intent).when(spy).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
        Mockito.doNothing().when(spy).unregisterReceiver(any(BroadcastReceiver.class));
    }

    @Test
    public void setVehiclesListTestWithNotNullList() {
        Mockito.doNothing().when(adapter).notifyDataSetChanged();

        // Call method
        spy.setVehicleList(vehicleList);

        // Verify something went wrong view is gone
        verify(somethingWentWrong).setVisibility(View.GONE);
        // Verify recycler view is visible
        verify(recyclerView).setVisibility(View.VISIBLE);
        // Verify broadcast receiver is unregistered
        verify(spy).unregisterReceiver(any(BroadcastReceiver.class));
        // Verify adapter notified
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void setVehiclesListTestWithNullList() {
        Mockito.doNothing().when(adapter).notifyDataSetChanged();

        // Call method
        spy.setVehicleList(null);

        // Verify something went wrong view is visible
        verify(somethingWentWrong).setVisibility(View.VISIBLE);
        // Verify recycler view is gone
        verify(recyclerView).setVisibility(View.GONE);
        // Verify adapter notified
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void onRetryAgainWhenNoInternetConnectionTest() {
        // Mock Internet connection
        PowerMockito.mockStatic(Utils.class);
        when(Utils.isNetworkAvailable(spy)).thenReturn(false);

        // Call method
        spy.onTryAgainClick(somethingWentWrong);

        // Verify
        verify(noInternetView).setVisibility(View.VISIBLE);
        verify(somethingWentWrong).setVisibility(View.GONE);
    }

    @Test
    public void onRetryAgainWhenInternetConnectionTest() {
        // Mock Internet connection
        PowerMockito.mockStatic(Utils.class);
        when(Utils.isNetworkAvailable(spy)).thenReturn(true);

        // Call method
        spy.onTryAgainClick(somethingWentWrong);

        // Verify
        verify(presenter).getVehicleList();
        verify(noInternetView).setVisibility(View.GONE);
        verify(somethingWentWrong).setVisibility(View.GONE);
    }

    @Test
    public void showProgressBarWithTrueTest() {
        // Call method
        spy.showProgress(true);
        // Verify
        verify(progressBar).setVisibility(View.VISIBLE);
    }

    @Test
    public void showProgressBarWithFalseTest() {
        // Call method
        spy.showProgress(false);
        // Verify
        verify(progressBar).setVisibility(View.GONE);
    }

    @Test
    public void onCreateWithNullSavedInstanceStateAndWithInternetTest() {
        // Mock Internet connection
        PowerMockito.mockStatic(Utils.class);
        when(Utils.isNetworkAvailable(spy)).thenReturn(true);

        when(bundle.getSerializable("VEHICLE_ARGS")).thenReturn(null);

        // Call method
        spy.onCreate(null);

        // Verify layout is set
        verify(spy).setContentView(R.layout.vehicle_list);
        // Verify vehicle list is fetched
        verify(presenter).getVehicleList();
        // Verify no internet view is hidden
        verify(noInternetView).setVisibility(View.GONE);
        // Verify something went wrong is gone
        verify(somethingWentWrong).setVisibility(View.GONE);
    }

    @Test
    public void onCreateWithNullSavedInstanceStateAndWithoutInternetTest() {
        // Mock No Internet connection
        PowerMockito.mockStatic(Utils.class);
        when(Utils.isNetworkAvailable(spy)).thenReturn(false);

        when(bundle.getSerializable("VEHICLE_ARGS")).thenReturn(null);

        // Call method
        spy.onCreate(null);

        // Verify layout is set
        verify(spy).setContentView(R.layout.vehicle_list);
        // Verify vehicle list is NOT fetched
        verify(presenter, times(0)).getVehicleList();
        // Verify no internet view is shown
        verify(noInternetView).setVisibility(View.VISIBLE);
        // Verify something went wrong is gone
        verify(somethingWentWrong).setVisibility(View.GONE);
    }

    @Test
    public void onCreateWithNonNullSavedInstanceStateTest() {
        when(bundle.getSerializable("VEHICLE_ARGS")).thenReturn(vehicleList);
        Mockito.doNothing().when(spy).setVehicleList(vehicleList);

        // Call method
        spy.onCreate(bundle);

        // Verify
        verify(spy).setContentView(R.layout.vehicle_list);
        verify(spy).setVehicleList(vehicleList);
        // Verify something went wrong is gone
        verify(somethingWentWrong).setVisibility(View.GONE);
    }

    @Test
    public void onDestroyShouldDetachViewFromPresenter() {
        // Call method
        spy.onDestroy();

        // Verify
        verify(presenter).detachView();
    }

    @Test
    public void onResumeShouldRegisterNetworkReceiverWhenVerticalListIsEmpty() {
        when(spy.getVehicleList()).thenReturn(null);

        // Call method
        spy.onResume();

        // Verify
        verify(spy).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void onResumeShouldRegisterNetworkReceiverWhenVerticalListIsNotEmpty() {
        when(spy.getVehicleList()).thenReturn(vehicleList);

        // Call method
        spy.onResume();

        // Verify
        verify(spy, times(0)).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void onPauseShouldUnRegisterNetworkReceiver() {
        // Call method
        spy.onPause();

        // Verify
        verify(spy).unregisterReceiver(any(BroadcastReceiver.class));
    }

    @Test
    public void handleNetworkChangeWhenConnectedAndPreviousStateIsNotConnected() {
        when(spy.isPreviousStateConnected()).thenReturn(false);

        // Call method
        spy.handleNetworkChange(true);

        // Verify
        verify(presenter).getVehicleList();
        verify(noInternetView).setVisibility(View.GONE);
    }

    @Test
    public void handleNetworkChangeWhenConnectedAndPreviousStateIsConnected() {
        when(spy.isPreviousStateConnected()).thenReturn(true);

        // Call method
        spy.handleNetworkChange(true);

        // Verify
        verify(presenter, times(0)).getVehicleList();
        verify(noInternetView).setVisibility(View.GONE);
    }

    @Test
    public void handleNetworkChangeWhenNotConnectedAndVerticalListEmpty() {
        when(spy.getVehicleList()).thenReturn(null);

        // Call method
        spy.handleNetworkChange(false);

        // Verify
        verify(presenter, times(0)).getVehicleList();
        verify(noInternetView).setVisibility(View.VISIBLE);
        verify(somethingWentWrong).setVisibility(View.GONE);
    }

    @Test
    public void handleNetworkChangeWhenNotConnectedAndVerticalListNotEmpty() {
        when(spy.getVehicleList()).thenReturn(vehicleList);

        // Call method
        spy.handleNetworkChange(false);

        // Verify
        verify(presenter, times(0)).getVehicleList();
        verify(noInternetView, times(0)).setVisibility(View.VISIBLE);
    }
}
