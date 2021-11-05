package com.elarrg.credit.repository.impl;

import com.elarrg.credit.model.util.Customer;
import com.elarrg.credit.repository.ICustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepositoryImpl implements ICustomerRepository {
    private final Map<String, Customer> userCreditResultMap;

    public CustomerRepositoryImpl() {
        userCreditResultMap = new ConcurrentHashMap<>();
    }

    public CustomerRepositoryImpl(Map<String, Customer> userCreditResultMap) {
        this.userCreditResultMap = userCreditResultMap;
    }

    @Override
    public Optional<Customer> findByIp(String ipAddress) {
        return Optional.ofNullable(userCreditResultMap.get(ipAddress));
    }

    @Override
    public Customer save(String ipAddress) {
        Customer newCustomer = new Customer(ipAddress);
        userCreditResultMap.putIfAbsent(ipAddress, newCustomer);
        return newCustomer;
    }
}
