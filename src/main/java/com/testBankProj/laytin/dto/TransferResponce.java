package com.testBankProj.laytin.dto;

import lombok.Data;
import lombok.NonNull;

@Data

public class TransferResponce {
    private boolean isOk;
    @NonNull
    private String message;
}
