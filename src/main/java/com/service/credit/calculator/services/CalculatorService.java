package com.service.credit.calculator.services;

import com.service.credit.calculator.dto.LoanPayment;
import com.service.credit.calculator.model.CreditCriteria;
import java.util.List;

public interface CalculatorService {
    /**
     * Возвращает платежи для погашения кредита
     *
     * @param criteria критерии кредита
     * @return платежи для погашения кредита
     */
    List<LoanPayment> getLoanPayments(CreditCriteria criteria);
}
