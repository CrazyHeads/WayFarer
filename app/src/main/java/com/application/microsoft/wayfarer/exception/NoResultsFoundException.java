package com.application.microsoft.wayfarer.exception;

import com.application.microsoft.wayfarer.exception.GooglePlacesException;
import com.application.microsoft.wayfarer.utils.Statuses;

public class NoResultsFoundException extends GooglePlacesException {
    public NoResultsFoundException(String errorMessage) {
        super(Statuses.STATUS_ZERO_RESULTS, errorMessage);
    }

    public NoResultsFoundException() {
        this(null);
    }
}
