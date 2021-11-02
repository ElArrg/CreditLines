package com.elarrg.credit.errors;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {

    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = List.of(error);
    }

    public static ApiError mapFrom(ServiceException serviceException) {
        HttpStatus httpStatus;

        switch (serviceException.getServiceExceptionType()) {
            case INVALID_ARGUMENT_ERROR:
            case MISSING_ARGUMENT_ERROR:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;

            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ApiError(httpStatus, serviceException.getErrorMessage(), serviceException.getErrorMessage());
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }
}
