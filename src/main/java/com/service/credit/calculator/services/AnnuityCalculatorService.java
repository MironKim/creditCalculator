package com.service.credit.calculator.services;

import com.service.credit.calculator.dto.LoanPayment;
import com.service.credit.calculator.model.CreditCriteria;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class AnnuityCalculatorService implements CalculatorService {

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final int MAIN_SCALE = 9;
    public static final int SEND_SCALE = 2;

    @Override
    public List<LoanPayment> getLoanPayments(CreditCriteria criteria) {
        List<LoanPayment> payments = new ArrayList<>();
        LocalDate date = LocalDate.now();
        BigDecimal amountOfCredit = criteria.getAmountOfCredit();
        Integer loanTermInMonths = criteria.getLoanTermInMonths();
        BigDecimal percent = criteria.getPercent();
        BigDecimal monthlyPayment = getMonthlyPayment(amountOfCredit, loanTermInMonths, percent);

        for (int month = 1; month < loanTermInMonths; month++) {
            BigDecimal percentPayment = getPercentOfAnnuityPayment(amountOfCredit, percent);
            BigDecimal principalDebt = monthlyPayment.subtract(percentPayment);
            LoanPayment payment = getLoanPayment(amountOfCredit, date, monthlyPayment, month, percentPayment,
                    principalDebt);
            amountOfCredit = amountOfCredit.subtract(principalDebt);
            payments.add(payment);
        }

        payments.add(getLastMonthLoanPayment(amountOfCredit, loanTermInMonths, date, percent));

        return payments;
    }

    /**
     * Возвращает ежемесячную процентную ставку в абсолютной величине P = Pg/12*100
     *
     * @param percent годовая процентная ставка (Pg)
     * @return ежемесячная процентная ставка (P)
     */
    private BigDecimal getMonthlyPercentRate(BigDecimal percent) {
        return percent.divide(valueOf(1200), MAIN_SCALE, ROUNDING_MODE);
    }

    /**
     * Возвращает аннуитетный платеж по кредиту для последнего месяца
     *
     * @param amountOfCredit   сумма кредита
     * @param loanTermInMonths срок кредита в месяцах
     * @param date             дата платежа
     * @param percent          годовая процентная ставка
     * @return аннуитетный платеж по кредиту
     */
    private LoanPayment getLastMonthLoanPayment(BigDecimal amountOfCredit, Integer loanTermInMonths, LocalDate date,
            BigDecimal percent) {
        BigDecimal percentPayment = getPercentOfAnnuityPayment(amountOfCredit, percent);
        BigDecimal lastMonthPayment = percentPayment.add(amountOfCredit);
        return getLoanPayment(amountOfCredit, date, lastMonthPayment, loanTermInMonths, percentPayment, amountOfCredit);
    }

    /**
     * Возвращает аннуитетный платеж по кредиту
     *
     * @param amountOfCredit остаток кредита
     * @param date           дата платежа
     * @param monthlyPayment общая сумма платежа
     * @param monthNumber    номер месяца по счету
     * @param percent        процентная состовляющая платежа
     * @param principalDebt  основная часть платежа в счет погашения кредита
     * @return аннуитетный платеж по кредиту
     */
    private LoanPayment getLoanPayment(BigDecimal amountOfCredit, LocalDate date, BigDecimal monthlyPayment,
            int monthNumber, BigDecimal percent, BigDecimal principalDebt) {
        return LoanPayment.builder()
                .number(monthNumber)
                .date(date.plusMonths(monthNumber).format(ofPattern("MM/yyyy")))
                .principalDebtPayment(principalDebt)
                .percentPayment(percent)
                .balanceOfCredit(amountOfCredit)
                .totalPaymentAmount(monthlyPayment)
                .build();
    }

    /**
     * Возвращает процентную составляющую аннуитетного платежа Pn= Sn*P
     *
     * @param amountOfCredit остаток задолжности (Sn)
     * @param percent        годовая процентная ставка по кредиту
     * @return начисленные проценты (Pn)
     */
    private BigDecimal getPercentOfAnnuityPayment(BigDecimal amountOfCredit, BigDecimal percent) {
        return amountOfCredit.multiply(getMonthlyPercentRate(percent)).setScale(SEND_SCALE, ROUNDING_MODE);
    }

    /**
     * Возвращает размер ежемесячного аннуитетного платежа по кредиту X = S*P/(1-(P + 1)^(-N) )
     *
     * @param amountOfCredit   сумма кредита (S)
     * @param loanTermInMonths срок кредита в месяцах (N)
     * @param percent          годовая процентная ставка
     * @return размер ежемесячного аннуитетного платежа (X)
     */
    private BigDecimal getMonthlyPayment(BigDecimal amountOfCredit, Integer loanTermInMonths, BigDecimal percent) {
        BigDecimal monthlyPercentRate = getMonthlyPercentRate(percent);
        BigDecimal numerator = amountOfCredit.multiply(monthlyPercentRate);
        BigDecimal pAdd1 = monthlyPercentRate.add(ONE);
        BigDecimal pAdd1PowN = ONE.divide(pAdd1.pow(loanTermInMonths), MAIN_SCALE, ROUNDING_MODE);
        BigDecimal denominator = ONE.subtract(pAdd1PowN);

        return numerator.divide(denominator, SEND_SCALE, ROUNDING_MODE);
    }
}
