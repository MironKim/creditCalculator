package com.service.credit.calculator.services;

import com.service.credit.calculator.dto.LoanPayment;
import com.service.credit.calculator.model.CreditCriteria;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.math.BigDecimal.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AnnuityCalculatorServiceTest {

    @Autowired
    private AnnuityCalculatorService service;

    @Test
    void getLoanPayments() {
        List<LoanPayment> actual = service.getLoanPayments(new CreditCriteria(valueOf(100), 1, valueOf(10)));
        List<LoanPayment> expected = getExpectedLoanPayment();
        assertEquals(expected, actual);
    }

    private List<LoanPayment> getExpectedLoanPayment() {
        return Collections.singletonList(LoanPayment
                .builder()
                .number(1)
                .date(LocalDate.now().plusMonths(1).format(ofPattern("MM/yyyy")))
                .principalDebtPayment(valueOf(100))
                .percentPayment(new BigDecimal("0.83"))
                .balanceOfCredit(valueOf(100))
                .totalPaymentAmount(new BigDecimal("100.83"))
                .build());
    }
}