package com.elarrg.credit.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
}
