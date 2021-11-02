package com.elarrg.credit.repository.impl;

import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.model.util.Customer;
import com.elarrg.credit.repository.ICreditLineRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CreditLineRepositoryImpl implements ICreditLineRepository {
    Map<Customer, CreditResult> customerCreditResultMap = new ConcurrentHashMap<>();

    @Override
    public Optional<CreditResult> findByCustomer(Customer customer) {
        return Optional.ofNullable(customerCreditResultMap.get(customer));
    }

    @Override
    public CreditResult save(Customer customer, CreditResult creditResult) {
        customer.setDefinitiveStatus(creditResult.getCreditLineStatus());
        return customerCreditResultMap.putIfAbsent(customer, creditResult);
    }
}
