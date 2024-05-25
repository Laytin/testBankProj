package com.testBankProj.laytin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Tag(name = "transfer request", description = "transfer request")
public class TransferRequest {
    @NotNull
    @Schema(name = "id of transfer initiator", example = "1",required = true)
    private int OwnerId;
    @NotNull
    @Schema(name = "destination username", example = "nelox1234",required = true)
    private String destUsername;
    @NotNull
    @Min(value = 0, message = "sum cannot be less than zero")
    @Schema(name = "transfer cost", example = "100.05",required = true)
    private float sum;
}
