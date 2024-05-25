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
@Table(name = "Phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "phone id", example = "1",required = true)
    private int id;
    @NonNull
    @Column(unique=true)
    private String phone;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Customer customer;

    public Phone(@NonNull String phone, @NonNull Customer customer) {
        this.phone = phone;
        this.customer = customer;
    }
}
