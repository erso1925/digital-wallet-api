package com.ersenpamuk.walletapi.entity;

import com.ersenpamuk.walletapi.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(unique = true, length = 11)
    private String tckn; // Turkish Identity Number, must be unique (used for login in real systems)

    @Enumerated(EnumType.STRING)
    private UserRole role; // Enum defining CUSTOMER or EMPLOYEE
}
