package com.elarrg.credit.service;

import com.elarrg.credit.errors.ServiceException;
import com.elarrg.credit.model.api.CreditRequest;
import com.elarrg.credit.model.api.CreditResult;
import com.elarrg.credit.model.util.Customer;

public interface ICreditLineService {
    CreditResult reviewCreditLineRequest(Customer customer, CreditRequest creditRequest) throws ServiceException;
}
