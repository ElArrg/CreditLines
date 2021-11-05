package com.elarrg.credit.service.impl;

import com.elarrg.credit.errors.ServiceException;
import com.elarrg.credit.model.api.BusinessType;
import com.elarrg.credit.model.api.CreditLineStatus;
import com.elarrg.credit.model.api.CreditRequest;
import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.model.util.Customer;
import com.elarrg.credit.repository.ICreditLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CreditLineServiceImplTest {

    @Mock
    ICreditLineRepository creditLineRepository;

    @InjectMocks
    CreditLineServiceImpl creditLineService;

    private Customer localIpCustomer;
    private CreditRequest currentCreditRequest;

    @BeforeEach
    void setUp() {
        localIpCustomer = new Customer("127.0.0.1");
        currentCreditRequest = new CreditRequest();
    }

    @Test
    void when_new_SME_review_is_one_fifth_of_monthly_revenue_then_accepted() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(creditLineRepository.save(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(847.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        CreditResult expectedCredit = new CreditResult(CreditLineStatus.ACCEPTED,
                currentCreditRequest.getRequestedCreditLine());

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_new_SME_review_is_higher_than_one_fifth_of_monthly_revenue_then_rejected() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());

        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(850.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);

        assertEquals(CreditLineStatus.REJECTED, actualCredit.getCreditLineStatus());
    }

    @Test
    void when_new_STARTUP_review_is_accepted_by_one_fifth_of_monthly_revenue() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(creditLineRepository.save(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        currentCreditRequest.setFoundingType(BusinessType.STARTUP);
        currentCreditRequest.setCashBalance(435.30);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(840.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        CreditResult expectedCredit = new CreditResult(CreditLineStatus.ACCEPTED,
                currentCreditRequest.getRequestedCreditLine());

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_new_STARTUP_review_is_accepted_by_one_third_of_monthly_revenue() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(creditLineRepository.save(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        currentCreditRequest.setFoundingType(BusinessType.STARTUP);
        currentCreditRequest.setCashBalance(4235.45);
        currentCreditRequest.setMonthlyRevenue(435.30);
        currentCreditRequest.setRequestedCreditLine(840.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        CreditResult expectedCredit = new CreditResult(CreditLineStatus.ACCEPTED,
                currentCreditRequest.getRequestedCreditLine());

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_new_STARTUP_review_is_rejected_by_exceeding() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());

        currentCreditRequest.setFoundingType(BusinessType.STARTUP);
        currentCreditRequest.setCashBalance(4235.45);
        currentCreditRequest.setMonthlyRevenue(435.30);
        currentCreditRequest.setRequestedCreditLine(2500.60);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        CreditResult expectedCredit = new CreditResult(CreditLineStatus.REJECTED);

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_review_was_already_accepted_then_get_first_accepted() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(creditLineRepository.save(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // First accepted call should be saved.
        Double expectedCreditLine = 847.00;
        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(expectedCreditLine);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());
        CreditResult acceptedCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        Mockito.when(creditLineRepository.findByCustomer(localIpCustomer)).thenReturn(Optional.of(acceptedCredit));

        // Second call should return the first accepted one even when it should be rejected.
        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(5000.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        CreditResult expectedCredit = new CreditResult(CreditLineStatus.ACCEPTED, expectedCreditLine);

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_after_failing_3_times_then_agent_will_reach() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(creditLineRepository.save(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(850.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());
        creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        // On third fail should get "agent reach" message
        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        CreditResult expectedCredit = new CreditResult(CreditLineStatus.REVIEWING);

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_applying_after_failing_3_times_then_agent_will_reach() throws ServiceException {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(creditLineRepository.save(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setMonthlyRevenue(4235.45);
        currentCreditRequest.setRequestedCreditLine(850.00);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());
        creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        // After 3rd fail should return an agent will reach you even if an acceptable application.
        CreditResult agentReach = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);
        Mockito.when(creditLineRepository.findByCustomer(localIpCustomer)).thenReturn(Optional.of(agentReach));
        CreditResult actualCredit = creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest);

        CreditResult expectedCredit = new CreditResult(CreditLineStatus.REVIEWING);

        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void when_new_SME_review_misses_monthly_revenue_then_error_shows() {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());

        currentCreditRequest.setFoundingType(BusinessType.SME);
        currentCreditRequest.setRequestedCreditLine(1000.98);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        ServiceException exception = assertThrows(ServiceException.class, () ->
                creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest));

        String expectedMessage = "Monthly revenue is required for a SME credit line application";
        String actualMessage = exception.getErrorMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void when_new_STARTUP_review_misses_cash_balance_then_error_shows() {
        Mockito.when(creditLineRepository.findByCustomer(Mockito.any()))
                .thenReturn(Optional.empty());

        currentCreditRequest.setFoundingType(BusinessType.STARTUP);
        currentCreditRequest.setMonthlyRevenue(1000.00);
        currentCreditRequest.setRequestedCreditLine(1000.98);
        currentCreditRequest.setRequestedDate(LocalDateTime.now());

        ServiceException exception = assertThrows(ServiceException.class, () ->
                creditLineService.reviewCreditLineRequest(localIpCustomer, currentCreditRequest));

        String expectedMessage = "Cash balance is required for a STARTUP credit line application";
        String actualMessage = exception.getErrorMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}