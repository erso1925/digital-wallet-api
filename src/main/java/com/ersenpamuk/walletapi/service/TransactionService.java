package com.ersenpamuk.walletapi.service;

import com.ersenpamuk.walletapi.entity.Transaction;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.OppositePartyType;
import com.ersenpamuk.walletapi.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    // Initiates a deposit transaction and sets appropriate status (PENDING or APPROVED)
    Transaction deposit(Wallet wallet, BigDecimal amount, String source, OppositePartyType sourceType);

    // Initiates a withdrawal transaction and updates wallet balances
    Transaction withdraw(Wallet wallet, BigDecimal amount, String destination, OppositePartyType destinationType);

    // Retrieves all transactions belonging to a wallet
    List<Transaction> getTransactionsByWallet(Wallet wallet);

    // Retrieves a transaction by its ID
    Optional<Transaction> findById(Long id);

    // Updates transaction status (e.g. PENDING → APPROVED or DENIED)
    Transaction updateStatus(Transaction transaction, TransactionStatus newStatus);
}
