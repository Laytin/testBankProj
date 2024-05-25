package com.testBankProj.laytin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Data
@NoArgsConstructor
@Entity
@Table(name = "email")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "email id", example = "1",required = true)
    private int id;
    @NonNull
    @Column(unique=true)
    private String email;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Customer customer;

    public Email(@NonNull String email, @NonNull Customer customer) {
        this.email = email;
        this.customer = customer;
    }
}
