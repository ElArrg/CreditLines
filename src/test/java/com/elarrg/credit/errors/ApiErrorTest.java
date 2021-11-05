package com.elarrg.credit.errors;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiErrorTest {

    @Test
    void when_map_from_invalid_argument_error_then_http_code_is_bad_request() {
        ServiceException ex = new ServiceException(ServiceException.ServiceExceptionType.INVALID_ARGUMENT_ERROR, "Invalid argument");
        ApiError error = ApiError.mapFrom(ex);

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
    }

    @Test
    void when_map_from_missing_argument_error_then_http_code_is_bad_request() {
        ServiceException ex = new ServiceException(ServiceException.ServiceExceptionType.MISSING_ARGUMENT_ERROR, "Missing argument");
        ApiError error = ApiError.mapFrom(ex);

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
    }
}