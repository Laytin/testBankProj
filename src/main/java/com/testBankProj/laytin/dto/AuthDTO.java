package com.testBankProj.laytin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
@Tag(name = "customer DTO", description = "Customer DTO")
@Data
public class AuthDTO {
    @NotNull
    @Size(min = 4, message = "too short username")
    @Size(max = 50, message = "too long username")
    @Schema(name = "Customer username", example = "lox1234", required = true)
    private String username;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$")
    @Schema(name = "Customer full name", example = "Ivan Ivanov Ivanovich", required = true)
    private String fullname;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    @Schema(name = "Customer DOB", example = "21.12.2002", required = true)
    private Date dateOfBirth;
    @NotNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Schema(name = "Customer email", required = true)
    private String email;
    @NotNull
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
    @Schema(name = "Customer phone List", required = true)
    private String phone;
    @NotNull
    @Size(min = 4, message = "too short password")
    @Schema(name = "Password. Encrypted", example = "before encryption: qwerty12345", required = true)
    private String password;
    @NotNull
    @Min(value = 0,message = "startbalance cannot be less than zero")
    @Schema(name = "Customer first deposit. Need to remember to limit max %",example = "100.50", required = true)
    private float firstDeposit;
}
