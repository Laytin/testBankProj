package com.testBankProj.laytin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class AuthDTO {
    @NotNull
    @Size(min = 4, message = "too short username")
    @Size(max = 50, message = "too long username")
    private String username;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$")
    private String fullname;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    private Date dateOfBirth;
    @NotNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
    @NotNull
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
    private String phone;
    @NotNull
    @Size(min = 4, message = "too short password")
    private String password;
    @NotNull
    @Min(value = 0,message = "startbalance cannot be less than zero")
    private float firstDeposit;
}
