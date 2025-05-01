package com.ersenpamuk.walletapi.entity;

import com.ersenpamuk.walletapi.enums.OppositePartyType;
import com.ersenpamuk.walletapi.enums.TransactionStatus;
import com.ersenpamuk.walletapi.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet; // Many transactions can belong to one wallet

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEPOSIT or WITHDRAW

    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartyType; // IBAN or PAYMENT

    private String oppositeParty; // IBAN or Payment ID

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, APPROVED, or DENIED

    private LocalDateTime createdAt = LocalDateTime.now(); // Automatically set on creation
}
