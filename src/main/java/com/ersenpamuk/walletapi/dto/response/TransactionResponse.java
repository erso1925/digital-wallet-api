package com.ersenpamuk.walletapi.dto.response;

import com.ersenpamuk.walletapi.enums.OppositePartyType;
import com.ersenpamuk.walletapi.enums.TransactionStatus;
import com.ersenpamuk.walletapi.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;

    private Long walletId;

    private BigDecimal amount;

    private TransactionType type;

    private OppositePartyType oppositePartyType;

    private String oppositeParty;

    private TransactionStatus status;

    private LocalDateTime createdAt;

    // DTO used to expose transaction details via API response
}
