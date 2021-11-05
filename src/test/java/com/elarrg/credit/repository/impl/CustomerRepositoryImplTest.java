package com.elarrg.credit.repository.impl;

import com.elarrg.credit.model.util.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryImplTest {
    @Mock
    private ConcurrentHashMap<String, Customer> userCreditResultMap;

    @InjectMocks
    private CustomerRepositoryImpl customerRepository;

    @Test
    void when_no_customer_associated_to_ip() {
        Mockito.when(userCreditResultMap.get(Mockito.anyString()))
                .thenReturn(null);

        Optional<Customer> optionalCustomer = customerRepository.findByIp("127.0.0.1");

        assertTrue(optionalCustomer.isEmpty());
    }

    @Test
    void when_customer_associated_to_ip_exists() {
        String customerIp = "127.0.0.1";
        Customer expectedCustomer = new Customer(customerIp);

        Mockito.when(userCreditResultMap.get(customerIp))
                .thenReturn(expectedCustomer);

        Optional<Customer> optionalCustomer = customerRepository.findByIp("127.0.0.1");

        assertEquals(Optional.of(expectedCustomer), optionalCustomer);
    }

    @Test
    void when_saving_a_new_ip() {
        Mockito.when(userCreditResultMap.putIfAbsent(Mockito.anyString(), Mockito.any()))
                .thenReturn(null);

        String expectedIp = "127.0.0.1";

        Customer newCustomer = customerRepository.save(expectedIp);

        assertEquals(expectedIp, newCustomer.getIpAddress());
    }

    @Test
    void when_saving_an_existing_ip() {
        Customer expectedCustomer = new Customer("127.0.0.1");

        Mockito.when(userCreditResultMap.putIfAbsent(Mockito.anyString(), Mockito.any()))
                .thenReturn(expectedCustomer);

        Customer actualCustomer = customerRepository.save(expectedCustomer.getIpAddress());

        assertNotEquals(expectedCustomer, actualCustomer);
    }
}