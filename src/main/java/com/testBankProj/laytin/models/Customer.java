package com.testBankProj.laytin.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="customer")
@Tag(name = "customer model", description = "Customer model")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Customer ID", example = "1", required = true)
    private int id;
    @Column(unique=true)
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
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @ToString.Exclude
    @NotNull
    @Schema(name = "Customer email list", required = true)
    private List<Email> emailList;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @ToString.Exclude
    @NotNull
    @Schema(name = "Customer phone List", required = true)
    private List<Phone> phoneList;
    @NotNull
    @Min(value = 0,message = "startbalance cannot be less than zero")
    @Schema(name = "Customer first deposit. Need to remember to limit max %",example = "100.50", required = true)
    private float firstDeposit;
    @NotNull
    @Min(value = 0,message = "currentBalance cannot be less than zero")
    @Schema(name = "Current balance (+%). Will init on create = firstDeposit", example = "100.50")
    private float currentBalance;
    @NotNull
    @Size(min = 4, message = "too short password")
    @Schema(name = "Password. Encrypted", example = "before encryption: qwerty12345", required = true)
    private String password;
}
