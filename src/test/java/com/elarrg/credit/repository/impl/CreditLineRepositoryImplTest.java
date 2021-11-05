package com.elarrg.credit.repository.impl;

import com.elarrg.credit.model.api.CreditLineStatus;
import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.model.util.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CreditLineRepositoryImplTest {
    @InjectMocks
    CreditLineRepositoryImpl creditLineRepository;
    @Mock
    private ConcurrentHashMap<Customer, CreditResult> customerCreditResultMap;

    @Test
    void when_not_found_customer() {
        Customer expectedCustomer = new Customer("127.0.0.1");
        Mockito.when(customerCreditResultMap.get(expectedCustomer)).thenReturn(null);

        Optional<CreditResult> optionalCreditResult = creditLineRepository.findByCustomer(expectedCustomer);

        assertTrue(optionalCreditResult.isEmpty());
    }

    @Test
    void when_found_customer_associated_to_credit_line() {
        Customer expectedCustomer = new Customer("127.0.0.1");
        CreditResult expectedCreditResult = new CreditResult(CreditLineStatus.REVIEWING);
        Mockito.when(customerCreditResultMap.get(expectedCustomer)).thenReturn(expectedCreditResult);

        Optional<CreditResult> optionalCreditResult = creditLineRepository.findByCustomer(expectedCustomer);

        assertEquals(Optional.of(expectedCreditResult), optionalCreditResult);
    }

    @Test
    void when_saving_a_new_credit_for_a_customer() {
        Mockito.when(customerCreditResultMap.putIfAbsent(Mockito.any(), Mockito.any()))
                .thenReturn(null);

        Customer newCustomer = new Customer("127.0.0.1");
        CreditResult expectedCreditResult = new CreditResult(CreditLineStatus.REVIEWING);

        CreditResult actualCreditResult = creditLineRepository.save(newCustomer, expectedCreditResult);

        assertEquals(expectedCreditResult, actualCreditResult);
    }

    @Test
    void when_saving_credit_for_an_existing_customer() {
        Customer newCustomer = new Customer("127.0.0.1");
        CreditResult expectedCreditResult = new CreditResult(CreditLineStatus.REVIEWING);

        Mockito.when(customerCreditResultMap.putIfAbsent(newCustomer, expectedCreditResult))
                .thenReturn(expectedCreditResult);

        CreditResult actualCreditResult = creditLineRepository.save(newCustomer, expectedCreditResult);

        assertEquals(expectedCreditResult, actualCreditResult);
    }
}