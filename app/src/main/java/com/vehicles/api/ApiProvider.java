package com.vehicles.api;

import android.support.annotation.VisibleForTesting;

public class ApiProvider {

    private static ApiProvider instance;

    private ApiEndpoint apiEndpoint;

    private ApiEndpoint mockApiEndpoint;

    private ApiProvider() {

    }

    public static ApiProvider getInstance() {
        if (instance == null) {
            instance = new ApiProvider();
        }
        return instance;
    }

    public ApiEndpoint getApiEndpoint() {
        if (mockApiEndpoint != null) {
            return mockApiEndpoint;
        }
        if (apiEndpoint == null) {
            apiEndpoint = new ApiEndpointImpl();
        }
        return apiEndpoint;
    }

    /**
     * Should be called only in test environment
     *
     * @param mockApiEndpoint - The mockApiEndpoint to use when UI Testing
     */
    @VisibleForTesting
    public void setMockApiEndpoint(ApiEndpoint mockApiEndpoint) {
        this.mockApiEndpoint = mockApiEndpoint;
    }
}
