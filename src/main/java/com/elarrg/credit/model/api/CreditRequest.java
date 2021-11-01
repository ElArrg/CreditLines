package com.elarrg.credit.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditRequest {
    private String foundingType;
    private Double cashBalance;
    private Double monthlyRevenue;
    private Double requestedCreditLine;
    private Timestamp requestedDate;

    public String getFoundingType() {
        return foundingType;
    }

    public void setFoundingType(String foundingType) {
        this.foundingType = foundingType;
    }

    public Double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(Double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Double getRequestedCreditLine() {
        return requestedCreditLine;
    }

    public void setRequestedCreditLine(Double requestedCreditLine) {
        this.requestedCreditLine = requestedCreditLine;
    }

    public Timestamp getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Timestamp requestedDate) {
        this.requestedDate = requestedDate;
    }
}
