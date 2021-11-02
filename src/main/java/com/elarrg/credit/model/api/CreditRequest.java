package com.elarrg.credit.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditRequest {

    @NotNull(message = "Business type is required")
    @ApiModelProperty(notes = "Business type", example = "SME", required = true)
    private BusinessType foundingType;

    @ApiModelProperty(notes = "The customer's bank account balance", example = "435.30")
    private Double cashBalance;

    @NotNull(message = "Monthly Revenue is required")
    @ApiModelProperty(notes = "The total sales revenue for the month", example = "4235.45", required = true)
    private Double monthlyRevenue;

    @NotNull(message = "The requested credit line is required")
    @ApiModelProperty(notes = "The requested credit Line", example = "100", required = true)
    private Double requestedCreditLine;

    @NotNull(message = "The requested date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @PastOrPresent(message = "Request date cannot be a future date")
    @ApiModelProperty(notes = "Represents when request was made", example = "2021-07-19T16:32:59.860Z", required = true)
    private LocalDateTime requestedDate;

    public BusinessType getFoundingType() {
        return foundingType;
    }

    public void setFoundingType(BusinessType foundingType) {
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

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }
}
