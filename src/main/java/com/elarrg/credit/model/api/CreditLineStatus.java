package com.elarrg.credit.model.api;

public enum CreditLineStatus {
    ACCEPTED("The credit line application is approved"),
    REJECTED("The credit line application is too high"),
    REVIEWING("A sales agent will contact you");

    private final String message;

    CreditLineStatus(String message) {
        this.message = message;
    }

    public String getStatus() {
        return name();
    }

    public String getMessage() {
        return message;
    }
}
