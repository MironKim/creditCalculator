package com.service.credit.calculator.model;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditCriteria {

    @NotNull(message = "Укажите сумму кредита")
    @Min(value = 100_000, message = "Сумма кредита не может быть меньше 100 000")
    @Max(value = 5_000_000, message = "Сумма кредита не может быть больше 5 000 000")
    private final BigDecimal amountOfCredit;

    @NotNull(message = "Укажите срок кредита в месяцах")
    @Min(value = 12, message = "Срок кредита не может быть меньше 12 месяцев")
    @Max(value = 60, message = "Срок кредита не может быть больше 60 месяцев")
    private final Integer loanTermInMonths;

    @NotNull(message = "Укажите годовую процентную ставку")
    @DecimalMin(value = "12.9", message = "Годовая процентная ставка не может быть меньше 12.9")
    @DecimalMax(value = "23.9", message = "Годовая процентная ставка не может быть больше 23.9")
    private final BigDecimal percent;

}
