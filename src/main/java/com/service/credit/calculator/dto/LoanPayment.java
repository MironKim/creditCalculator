package com.service.credit.calculator.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanPayment {
    /**
     * Номер платежа
     */
    private int number;
    /**
     * Месяц/Год
     */
    private String date;
    /**
     * Платеж по основному долгу
     */
    private BigDecimal principalDebtPayment;
    /**
     * Процентная составляющая платежа
     */
    private BigDecimal percentPayment;
    /**
     * Остаток кредита
     */
    private BigDecimal balanceOfCredit;
    /**
     * Общая сумма платежа
     */
    private BigDecimal totalPaymentAmount;
}
