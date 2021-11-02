package com.elarrg.credit.errors;

public class ServiceException extends Exception {

    private final ServiceExceptionType serviceExceptionType;
    private final String errorMessage;

    public ServiceException(ServiceExceptionType serviceExceptionType, String errorMessage) {
        this(serviceExceptionType, errorMessage, null);
    }

    public ServiceException(ServiceExceptionType serviceExceptionType, String errorMessage, Throwable t) {
        super(errorMessage, t);
        this.serviceExceptionType = serviceExceptionType;
        this.errorMessage = errorMessage;
    }

    public ServiceExceptionType getServiceExceptionType() {
        return serviceExceptionType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public enum ServiceExceptionType {
        MISSING_ARGUMENT_ERROR("Parameter is missing"),
        INVALID_ARGUMENT_ERROR("Parameter used is invalid");

        private final String reasonPhrase;

        ServiceExceptionType(String reasonPhrase) {
            this.reasonPhrase = reasonPhrase;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }
    }
}
