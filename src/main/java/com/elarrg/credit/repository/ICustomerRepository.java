package com.elarrg.credit.repository;

import com.elarrg.credit.model.util.Customer;

import java.util.Optional;

public interface ICustomerRepository {
    Optional<Customer> findByIp(String ipAddress);

    Customer save(String ipAddress);
}
