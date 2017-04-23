package com.vehicles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vehicles.api.ApiProvider;
import com.vehicles.model.VehicleList;
import com.vehicles.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleListActivity extends AppCompatActivity implements VehiclesContract.VehiclesListView {

    private VehicleList vehicleList;

    private VehicleListPresenter presenter;

    private VehicleListAdapter adapter;

    private boolean isPreviousStateConnected = false;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.vehicles_list) RecyclerView recyclerView;

    @BindView(R.id.no_internet) View noInternetView;

    @BindView(R.id.something_went_wrong) View somethingWentWrong;

    @OnClick(R.id.try_again_button)
    public void onTryAgainClick(View view) {
        getSomethingWentWrong().setVisibility(View.GONE);
        if (Utils.isNetworkAvailable(this)) {
            getPresenter().getVehicleList();
            getNoInternetView().setVisibility(View.GONE);
        } else {
            getNoInternetView().setVisibility(View.VISIBLE);
        }
    }

    static final String VEHICLE_ARGS = "VEHICLE_ARGS";

    static final String IS_PREVIOUS_STATE_CONNECTED = "IS_PREVIOUS_STATE_CONNECTED";

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                handleNetworkChange(true);
            } else {
                handleNetworkChange(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_list);
        ButterKnife.bind(this);
        adapter = new VehicleListAdapter();
        getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        getRecyclerView().setAdapter(adapter);
        presenter = new VehicleListPresenter(this, ApiProvider.getInstance().getApiEndpoint());
        getSomethingWentWrong().setVisibility(View.GONE);
        if (savedInstanceState == null) {
            if (Utils.isNetworkAvailable(this)) {
                getPresenter().getVehicleList();
                getNoInternetView().setVisibility(View.GONE);
            } else {
                getNoInternetView().setVisibility(View.VISIBLE);
            }
        } else {
            isPreviousStateConnected = savedInstanceState.getBoolean(IS_PREVIOUS_STATE_CONNECTED);
            setVehicleList((VehicleList) savedInstanceState.getSerializable(VEHICLE_ARGS));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(VEHICLE_ARGS, vehicleList);
        outState.putSerializable(IS_PREVIOUS_STATE_CONNECTED, isPreviousStateConnected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getVehicleList() == null) {
            registerReceiver(networkStateReceiver, getNetworkStateIntentFilter());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(networkStateReceiver);
        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public void showProgress(boolean show) {
        getProgressBar().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setVehicleList(VehicleList vehicleList) {
        this.vehicleList = vehicleList;
        if (vehicleList == null) {
            getSomethingWentWrong().setVisibility(View.VISIBLE);
            getRecyclerView().setVisibility(View.GONE);
        } else {
            unregisterReceiver();
            getRecyclerView().setVisibility(View.VISIBLE);
            getSomethingWentWrong().setVisibility(View.GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onVehicleClick(int position) {
        Intent intent = new Intent(this, VehicleDetailActivity.class);
        intent.putExtra(VEHICLE_ARGS, vehicleList.vehicles.get(position));
        startActivity(intent);
    }

    void handleNetworkChange(boolean isConnected) {
        if (isConnected) {
            if (!isPreviousStateConnected() && getPresenter() != null) {
                getPresenter().getVehicleList();
            }
            if (getNoInternetView() != null) {
                getNoInternetView().setVisibility(View.GONE);
            }
            setPreviousStateConnected(true);
        } else {
            if (getNoInternetView() != null && getVehicleList() == null) {
                getNoInternetView().setVisibility(View.VISIBLE);
                getSomethingWentWrong().setVisibility(View.GONE);
            }
            setPreviousStateConnected(false);
        }
    }

    // Create Getters to facilitate mocking in unit tests
    VehicleListPresenter getPresenter() {
        return presenter;
    }

    RecyclerView getRecyclerView() {
        return recyclerView;
    }

    VehicleListAdapter getAdapter() {
        return adapter;
    }

    ProgressBar getProgressBar() {
        return progressBar;
    }

    View getNoInternetView() {
        return noInternetView;
    }

    View getSomethingWentWrong() {
        return somethingWentWrong;
    }

    boolean isPreviousStateConnected() {
        return isPreviousStateConnected;
    }

    VehicleList getVehicleList() {
        return vehicleList;
    }

    void setPreviousStateConnected(boolean previousStateConnected) {
        isPreviousStateConnected = previousStateConnected;
    }

    IntentFilter getNetworkStateIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return filter;
    }

    class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

        @Override
        public VehicleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VehicleListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item, parent, false));
        }

        @Override
        public void onBindViewHolder(VehicleListAdapter.ViewHolder holder, int position) {
            holder.vehicleName.setText(vehicleList.vehicles.get(position).vrn);
        }

        @Override
        public int getItemCount() {
            return vehicleList == null || vehicleList.vehicles.size() == 0 ?
                    0 : vehicleList.vehicles.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.vehicle_name) TextView vehicleName;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onVehicleClick(getAdapterPosition());
                    }
                });
            }
        }
    }
}
