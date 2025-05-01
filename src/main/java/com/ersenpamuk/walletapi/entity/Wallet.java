package com.ersenpamuk.walletapi.entity;

import com.ersenpamuk.walletapi.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer; // Each wallet belongs to a customer

    private String walletName;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency; // Supported: TRY, USD, EUR

    private boolean activeForShopping; // Flag to enable/disable shopping with this wallet

    private boolean activeForWithdraw; // Flag to enable/disable withdrawals from this wallet

    private BigDecimal balance = BigDecimal.ZERO; // Total balance, including PENDING funds

    private BigDecimal usableBalance = BigDecimal.ZERO; // Available balance for spending or withdrawing
}
