package com.ersenpamuk.walletapi.dto;

import com.ersenpamuk.walletapi.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTransactionStatusRequest {

    @NotNull(message = "Transaction ID is required.")
    private Long transactionId;

    @NotNull(message = "New status is required.")
    private TransactionStatus newStatus;

    // DTO used to approve or deny a PENDING transaction
}
