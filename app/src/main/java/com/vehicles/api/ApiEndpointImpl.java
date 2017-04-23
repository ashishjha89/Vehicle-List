package com.vehicles.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicles.BuildConfig;
import com.vehicles.VehiclesContract;
import com.vehicles.model.VehicleList;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class ApiEndpointImpl implements ApiEndpoint {

    private ApiService apiService;

    private static ObjectMapper camelCaseMapper;

    @Override
    public void getVehicleList(final VehiclesContract.OnVehicleListCallback callback) {
        Listener<VehicleList> onSuccess = new Listener<VehicleList>() {
            @Override
            public void onResponse(VehicleList response) {
                if (response.vehicles != null && response.vehicles.size() > 0) {
                    callback.onVehicleListLoaded(response);
                } else {
                    callback.onVehicleListLoaded(null);
                }
            }
        };
        ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(NetworkError error) {
                callback.onVehicleListLoaded(null);
            }
        };
        enqueueRequest(getApiService().getVehicleList(), onSuccess, onError);
    }

    private <T> void enqueueRequest(Call<T> call, final Listener<T> listener, final ErrorListener errorListener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onResponse(response.body());
                    }
                } else if (errorListener != null) {
                    errorListener.onErrorResponse(new NetworkError("Unknown error"));
                }

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (call.isCanceled()) {
                    return;
                }
                if (errorListener != null) {
                    errorListener.onErrorResponse(new NetworkError(t));
                }
            }

        });
    }

    private ApiService getApiService() {
        if (apiService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.connectTimeout(30, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.valueOf("BODY"));
                builder.addInterceptor(logging);
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl() + "/")
                    .addConverterFactory(JacksonConverterFactory.create(getCamelCase()))
                    .callFactory(builder.build())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static ObjectMapper getCamelCase() {
        if (camelCaseMapper == null) {
            camelCaseMapper = new ObjectMapper();
            // We don't want the app to throw an exception (and stop processing) when an unknown/new JSON property/field comes by
            camelCaseMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return camelCaseMapper;
    }

    private static String getBaseUrl() {
        return "http://private-6d86b9-vehicles5.apiary-mock.com";
    }
}
