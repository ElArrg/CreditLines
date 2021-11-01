package com.elarrg.credit.controller;

import com.elarrg.credit.model.api.CreditRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CreditModelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditModelController.class);

    @PostMapping(value = {"/credit/request"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void requestCreditLine(@RequestBody CreditRequest creditRequest) {
        // Not implemented yet
        throw new UnsupportedOperationException();
    }

}
