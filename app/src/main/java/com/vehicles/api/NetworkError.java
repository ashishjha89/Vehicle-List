package com.vehicles.api;

import okhttp3.ResponseBody;

/**
 * Exception style class encapsulating Volley errors
 */
@SuppressWarnings("serial")
class NetworkError extends Exception {

    ResponseBody responseBody;
    /**
     * The HTTP status code.
     */
     int statusCode;

    NetworkError(String exceptionMessage) {
        super(exceptionMessage);
    }

    NetworkError(Throwable cause) {
        super(cause);
    }

    NetworkError(ResponseBody responseBody, int code) {
        this.responseBody = responseBody;
        statusCode = code;
    }
}
