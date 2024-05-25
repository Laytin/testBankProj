package com.testBankProj.laytin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NonNull;

@Data
@Tag(name = "trasnfer responce", description = "trasnfer responce")
public class TransferResponce {
    @Schema(name = "transfer statement", example = "true")
    private boolean isOk;
    @NonNull
    @Schema(name = "transfer message", example = "Transfer sucsess!")
    private String message;
}
