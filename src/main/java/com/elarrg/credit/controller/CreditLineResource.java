package com.elarrg.credit.controller;

import com.elarrg.credit.errors.ServiceException;
import com.elarrg.credit.model.api.CreditRequest;
import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.service.ICreditLineService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/credits")
public class CreditLineResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditLineResource.class);

    private final ICreditLineService creditLineService;

    public CreditLineResource(ICreditLineService creditLineService) {
        this.creditLineService = creditLineService;
    }

    @ApiOperation(value = "Request a credit line", response = CreditResult.class)
    @PostMapping(value = {"/applications"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditResult requestCreditLine(@RequestBody @Valid CreditRequest creditRequest) throws ServiceException {
        return creditLineService.reviewCreditLineRequest(creditRequest);
    }

}
