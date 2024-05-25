package com.testBankProj.laytin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDTO {
    @Schema(name = "Customer id", example = "1")
    private int id;
    @Schema(name = "Customer username", example = "lox1234")
    private String username;
    @Schema(name = "Customer full name", example = "Ivan Ivanov Ivanovich")
    private String fullname;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Schema(name = "Customer DOB", example = "21.12.2002")
    private Date dateOfBirth;
    @Schema(name = "Customer email list")
    private List<Email> emailList;
    @Schema(name = "Customer phone List")
    private List<Phone> phoneList;
    @Schema(name = "Customer first deposit. Need to remember to limit max %",example = "100.50")
    private float firstDeposit;
    @Schema(name = "Current balance (+%). Will init on create = firstDeposit", example = "100.50")
    private float currentBalance;
}
