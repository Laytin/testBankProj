package com.testBankProj.laytin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private int id;
    private String username;
    private String fullname;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private List<Email> emailList;
    private List<Phone> phoneList;
    private float firstDeposit;
    private float currentBalance;
}
