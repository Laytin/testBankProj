package com.testBankProj.laytin.utils.errors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorBuilder {
    public static void buildErrorMessageForClient(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                    .append(";");
        }
        throw new DefaultErrorException(errorMsg.toString());
    }
    public static void buildErrorMessageForClient(String message) {
        throw new DefaultErrorException(message);
    }
}
