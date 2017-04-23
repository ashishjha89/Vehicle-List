package com.vehicles.api;

/**
 * Callback interface for delivering error responses.
 */
interface ErrorListener {
    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    void onErrorResponse(NetworkError error);
}
