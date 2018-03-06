package com.application.microsoft.wayfarer.exception;

import com.application.microsoft.wayfarer.exception.GooglePlacesException;
import com.application.microsoft.wayfarer.utils.Statuses;



public class RequestDeniedException extends GooglePlacesException {
    public RequestDeniedException(String errorMessage) {
        super(Statuses.STATUS_REQUEST_DENIED, errorMessage);
       // super(Statuses);

    }

    public RequestDeniedException() {
        this(null);
    }
}