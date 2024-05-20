package com.testBankProj.laytin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferRequest {
    @NotNull
    private int OwnerId;
    @NotNull
    private String destUsername;
    @NotNull
    @Min(value = 0, message = "sum caanot be less than zero")
    private float sum;
}
