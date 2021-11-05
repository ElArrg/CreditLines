package com.elarrg.credit.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditResult {

    @ApiModelProperty(notes = "Status of the credit line application", required = true)
    private final CreditLineStatus creditLineStatus;

    @ApiModelProperty(notes = "Approved credit line", example = "100")
    private Double creditLine;

    public CreditResult(CreditLineStatus creditLineStatus) {
        this.creditLineStatus = creditLineStatus;
    }

    public CreditResult(CreditLineStatus creditLineStatus, Double creditLine) {
        this(creditLineStatus);
        this.creditLine = creditLine;
    }

    public CreditLineStatus getCreditLineStatus() {
        return creditLineStatus;
    }


    @ApiModelProperty(notes = "Description of the credit line application result", required = true)
    public String getMessage() {
        return creditLineStatus.getMessage();
    }

    public Double getCreditLine() {
        return creditLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditResult that = (CreditResult) o;
        return creditLineStatus == that.creditLineStatus && Objects.equals(creditLine, that.creditLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditLineStatus, creditLine);
    }
}
