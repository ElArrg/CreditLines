package com.elarrg.credit.service;

import com.elarrg.credit.errors.ServiceException;
import com.elarrg.credit.model.api.CreditRequest;
import com.elarrg.credit.model.api.CreditResult;

public interface ICreditLineService {
    CreditResult reviewCreditLineRequest(CreditRequest creditRequest) throws ServiceException;
}
