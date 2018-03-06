package com.application.microsoft.wayfarer.exception;

import com.application.microsoft.wayfarer.exception.GooglePlacesException;
import com.application.microsoft.wayfarer.utils.Statuses;

public class InvalidRequestException extends GooglePlacesException {
    public InvalidRequestException(String errorMessage) {
        super(Statuses.STATUS_INVALID_REQUEST, errorMessage);
    }

    public InvalidRequestException() {
        this(null);
    }
}
