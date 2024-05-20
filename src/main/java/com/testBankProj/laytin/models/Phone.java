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
@Tag(name = "Phone model", description = "Phone model")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "phone id", example = "1",required = true)
    private int id;
    @NonNull
    @Column(unique=true)
    @NotNull
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
    @Schema(name = "phone", example = "+375252555522",required = true)
    private String phone;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    @Schema(name = "customer key",required = true)
    private Customer customer;

    public Phone(@NonNull String phone, @NonNull Customer customer) {
        this.phone = phone;
        this.customer = customer;
    }
}
