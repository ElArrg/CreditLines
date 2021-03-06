package com.elarrg.credit.service.impl;

import com.elarrg.credit.errors.ServiceException;
import com.elarrg.credit.model.api.BusinessType;
import com.elarrg.credit.model.api.CreditLineStatus;
import com.elarrg.credit.model.api.CreditRequest;
import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.model.util.Customer;
import com.elarrg.credit.repository.ICreditLineRepository;
import com.elarrg.credit.service.ICreditLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.elarrg.credit.errors.ServiceException.ServiceExceptionType;

@Service
public class CreditLineServiceImpl implements ICreditLineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditLineServiceImpl.class);

    private static final Integer CASH_BALANCE_PARTS = 1;
    private static final Integer CASH_BALANCE_PARTITION = 3;

    private static final Integer MONTHLY_REVENUE_PARTS = 1;
    private static final Integer MONTHLY_REVENUE_PARTITION = 5;

    private final ICreditLineRepository creditLineRepository;

    public CreditLineServiceImpl(ICreditLineRepository creditLineRepository) {
        this.creditLineRepository = creditLineRepository;
    }

    public CreditResult reviewCreditLineRequest(Customer customer, CreditRequest creditRequest) throws ServiceException {

        Optional<CreditResult> optionalCreditResult = creditLineRepository.findByCustomer(customer);

        if (optionalCreditResult.isPresent()) {
            return optionalCreditResult.get();
        }

        Double recommendedCreditLine = getRecommendedCreditLine(
                creditRequest.getFoundingType(),
                creditRequest.getMonthlyRevenue(),
                creditRequest.getCashBalance()
        );

        CreditResult creditResult;

        if (isAcceptableCreditLine(
                creditRequest.getRequestedCreditLine(),
                recommendedCreditLine
        )) {
            creditResult = creditLineRepository.save(customer,
                    new CreditResult(CreditLineStatus.ACCEPTED, creditRequest.getRequestedCreditLine()));

        } else {

            if (customer.incrementFailureCount() >= Customer.APPLICATION_FAILURE_LIMIT) {
                creditResult = creditLineRepository.save(customer, new CreditResult(CreditLineStatus.REVIEWING));
            } else {
                creditResult = new CreditResult(CreditLineStatus.REJECTED);
            }
        }

        return creditResult;
    }

    private boolean isAcceptableCreditLine(Double requestedCreditLine, Double recommendedCreditLine) {
        return recommendedCreditLine >= requestedCreditLine;
    }

    private Double getRecommendedCreditLine(BusinessType businessType, Double monthlyRevenue, Double cashBalance) throws ServiceException {

        if (monthlyRevenue == null) {
            String message = String.format("Monthly revenue is required for a %s credit line application", businessType);
            throw new ServiceException(ServiceExceptionType.MISSING_ARGUMENT_ERROR, message);
        }

        double monthlyRevenueProportion = (monthlyRevenue / MONTHLY_REVENUE_PARTITION) * MONTHLY_REVENUE_PARTS;

        switch (businessType) {
            case SME: {
                return monthlyRevenueProportion;
            }

            case STARTUP: {

                if (cashBalance == null) {
                    String message = String.format("Cash balance is required for a %s credit line application", businessType);
                    throw new ServiceException(ServiceExceptionType.MISSING_ARGUMENT_ERROR, message);
                }

                double cashBalanceProportion = (cashBalance / CASH_BALANCE_PARTITION) * CASH_BALANCE_PARTS;
                return Math.max(monthlyRevenueProportion, cashBalanceProportion);
            }

            default: {
                // This case shouldn't be executed
                String message = String.format("%s credit line applications are not currently supported", businessType);
                throw new ServiceException(ServiceExceptionType.INVALID_ARGUMENT_ERROR, message);
            }
        }

    }

}
