package com.service.credit.calculator.controllers;

import com.service.credit.calculator.dto.LoanPayment;
import com.service.credit.calculator.model.CreditCriteria;
import com.service.credit.calculator.services.AnnuityCalculatorService;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    private static String date = "12/2020";
    private static Random random = new Random();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnnuityCalculatorService service;

    @Test
    void getLoanPayment() throws Exception {
        CreditCriteria creditCriteria = new CreditCriteria(valueOf(100), 10, valueOf(15));
        List<LoanPayment> loanPayments = getPaymentList();
        LoanPayment loanPayment = loanPayments.get(0);
        when(service.getLoanPayments(eq(creditCriteria))).thenReturn(loanPayments);

        mockMvc.perform(get("/calculator")
                .param("amountOfCredit", creditCriteria.getAmountOfCredit().toString())
                .param("loanTermInMonths", creditCriteria.getLoanTermInMonths().toString())
                .param("percent", creditCriteria.getPercent().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(loanPayment.getNumber())))
                .andExpect(jsonPath("$[0].date", is(loanPayment.getDate())))
                .andExpect(jsonPath("$[0].principalDebtPayment", is(loanPayment.getPrincipalDebtPayment().intValue())))
                .andExpect(jsonPath("$[0].percentPayment", is(loanPayment.getPercentPayment().intValue())))
                .andExpect(jsonPath("$[0].balanceOfCredit", is(loanPayment.getBalanceOfCredit().intValue())))
                .andExpect(jsonPath("$[0].totalPaymentAmount", is(loanPayment.getTotalPaymentAmount().intValue())));

    }

    private List<LoanPayment> getPaymentList() {
        return Collections.singletonList(LoanPayment
                .builder()
                .number(random.nextInt(100))
                .date(date)
                .principalDebtPayment(valueOf(random.nextInt(100)))
                .percentPayment(valueOf(random.nextInt(100)))
                .balanceOfCredit(valueOf(random.nextInt(100)))
                .totalPaymentAmount(valueOf(random.nextInt(100)))
                .build());
    }
}