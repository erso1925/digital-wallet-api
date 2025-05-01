package com.ersenpamuk.walletapi.dto;

import com.ersenpamuk.walletapi.enums.OppositePartyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class WithdrawRequest {

    @NotNull(message = "Wallet ID must not be null.")
    private Long walletId;

    @NotNull(message = "Amount must not be null.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @NotNull(message = "Destination must not be null.")
    private String destination;

    @NotNull(message = "Destination type must be specified.")
    private OppositePartyType destinationType;

    // DTO used to receive withdrawal requests via API
}
