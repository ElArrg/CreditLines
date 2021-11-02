package com.elarrg.credit.repository;

import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.model.util.Customer;

import java.util.Optional;

public interface ICreditLineRepository {
    Optional<CreditResult> findByCustomer(Customer customer);

    CreditResult save(Customer customer, CreditResult creditResult);
}
