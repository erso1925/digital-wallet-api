package com.ersenpamuk.walletapi.dto;

import com.ersenpamuk.walletapi.enums.CurrencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateWalletRequest {

    @NotBlank(message = "Wallet name is required.")
    private String walletName;

    @NotNull(message = "Currency is required.")
    private CurrencyType currency;

    @NotNull(message = "activeForShopping must be specified.")
    private Boolean activeForShopping;

    @NotNull(message = "activeForWithdraw must be specified.")
    private Boolean activeForWithdraw;

    // DTO used to receive wallet creation requests from API consumers
}
