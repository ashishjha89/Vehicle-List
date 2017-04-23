package com.vehicles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.vehicles.model.Vehicle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehicleDetailActivity extends AppCompatActivity {

    @BindView(R.id.vehicle_id_value) TextView vehicleValue;
    @BindView(R.id.vrn_value) TextView vrnValue;
    @BindView(R.id.country_value) TextView countryValue;
    @BindView(R.id.color_value) TextView colorValue;
    @BindView(R.id.type_value) TextView typeValue;
    @BindView(R.id.default_value) TextView defaultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_detail);
        ButterKnife.bind(this);
        Vehicle vehicle = (Vehicle) getIntent().getSerializableExtra(VehicleListActivity.VEHICLE_ARGS);
        setupViews(vehicle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(vehicle.vrn);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews(Vehicle vehicle) {
        vehicleValue.setText(vehicle.vehicleId);
        vrnValue.setText(vehicle.vrn);
        countryValue.setText(vehicle.country);
        colorValue.setText(vehicle.color);
        typeValue.setText(vehicle.type);
        defaultValue.setText(String.valueOf(vehicle.isDefault));
    }
}
