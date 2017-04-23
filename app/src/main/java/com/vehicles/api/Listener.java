package com.vehicles.api;

/**
 * Callback interface for delivering parsed responses.
 */
interface Listener<T> {
    /**
     * Called when a response is received.
     */
    void onResponse(T response);
}