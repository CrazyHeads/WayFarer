package com.application.microsoft.wayfarer.exception;

import com.application.microsoft.wayfarer.exception.GooglePlacesException;
import com.application.microsoft.wayfarer.utils.Statuses;

public class OverQueryLimitException extends GooglePlacesException {
    public OverQueryLimitException(String errorMessage) {
        super(Statuses.STATUS_OVER_QUERY_LIMIT, errorMessage);
    }

    public OverQueryLimitException() {
        this(null);
    }
}
