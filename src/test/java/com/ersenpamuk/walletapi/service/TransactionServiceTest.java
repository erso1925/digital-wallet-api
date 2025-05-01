package com.ersenpamuk.walletapi.service;

import com.ersenpamuk.walletapi.entity.Transaction;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.OppositePartyType;
import com.ersenpamuk.walletapi.enums.TransactionStatus;
import com.ersenpamuk.walletapi.enums.TransactionType;
import com.ersenpamuk.walletapi.repository.TransactionRepository;
import com.ersenpamuk.walletapi.repository.WalletRepository;
import com.ersenpamuk.walletapi.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Test
    public void shouldDepositAmountUnder1000AndApprove() {
        // Arrange
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository, walletRepository);

        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsableBalance(BigDecimal.ZERO);

        BigDecimal amount = BigDecimal.valueOf(500);

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setWallet(wallet);
        expectedTransaction.setAmount(amount);
        expectedTransaction.setType(TransactionType.DEPOSIT);
        expectedTransaction.setStatus(TransactionStatus.APPROVED);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        // Act
        Transaction transaction = transactionService.deposit(wallet, amount, "TR1234567890", OppositePartyType.IBAN);

        // Assert
        assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        verify(walletRepository, times(1)).save(wallet);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
