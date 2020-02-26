package com.service.credit.calculator.controllers;

import com.service.credit.calculator.dto.LoanPayment;
import com.service.credit.calculator.model.CreditCriteria;
import com.service.credit.calculator.services.CalculatorService;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService service;

    @GetMapping("/calculator")
    public ResponseEntity<List<LoanPayment>> getLoanPayment(@Valid CreditCriteria criteria) {
        return ResponseEntity.ok(service.getLoanPayments(criteria));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<String>> handleError(Exception e) {
        if (e instanceof BindException) {
            List<ObjectError> allErrors = ((BindException) e).getBindingResult().getAllErrors();
            List<String> errorMessages = allErrors.stream().map(ObjectError::getDefaultMessage).collect(toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(Collections.singletonList("Внутреняя ошибка сервера"));
    }

}

