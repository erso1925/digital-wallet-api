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
public class DepositRequest {

    @NotNull(message = "Wallet ID must not be null.")
    private Long walletId;

    @NotNull(message = "Amount must not be null.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @NotNull(message = "Source must not be null.")
    private String source;

    @NotNull(message = "Source type must be specified.")
    private OppositePartyType sourceType;

    // DTO used to receive deposit requests via API
}
