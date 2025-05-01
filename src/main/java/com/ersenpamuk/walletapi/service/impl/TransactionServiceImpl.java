package com.ersenpamuk.walletapi.service.impl;

import com.ersenpamuk.walletapi.entity.Transaction;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.OppositePartyType;
import com.ersenpamuk.walletapi.enums.TransactionStatus;
import com.ersenpamuk.walletapi.enums.TransactionType;
import com.ersenpamuk.walletapi.repository.TransactionRepository;
import com.ersenpamuk.walletapi.repository.WalletRepository;
import com.ersenpamuk.walletapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Transaction deposit(Wallet wallet, BigDecimal amount, String source, OppositePartyType sourceType) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setOppositeParty(source);
        transaction.setOppositePartyType(sourceType);
        transaction.setCreatedAt(LocalDateTime.now());

        // Amount > 1000 → PENDING; else → APPROVED
        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            transaction.setStatus(TransactionStatus.PENDING);
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            transaction.setStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().add(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
        }

        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction withdraw(Wallet wallet, BigDecimal amount, String destination, OppositePartyType destinationType) {
        if (!wallet.isActiveForWithdraw() && !wallet.isActiveForShopping()) {
            throw new IllegalStateException("This wallet is not active for withdraw or shopping.");
        }

        if (wallet.getUsableBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient usable balance for withdrawal.");
        }

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setOppositeParty(destination);
        transaction.setOppositePartyType(destinationType);
        transaction.setCreatedAt(LocalDateTime.now());

        // Amount > 1000 → PENDING; else → APPROVED
        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            transaction.setStatus(TransactionStatus.PENDING);
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        } else {
            transaction.setStatus(TransactionStatus.APPROVED);
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }

        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByWallet(Wallet wallet) {
        return transactionRepository.findByWallet(wallet);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction updateStatus(Transaction transaction, TransactionStatus newStatus) {
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only PENDING transactions can be updated.");
        }

        Wallet wallet = transaction.getWallet();
        BigDecimal amount = transaction.getAmount();

        if (newStatus == TransactionStatus.APPROVED) {
            transaction.setStatus(TransactionStatus.APPROVED);

            if (transaction.getType() == TransactionType.DEPOSIT) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                wallet.setBalance(wallet.getBalance().subtract(amount));
            }

        } else if (newStatus == TransactionStatus.DENIED) {
            transaction.setStatus(TransactionStatus.DENIED);

            if (transaction.getType() == TransactionType.DEPOSIT) {
                wallet.setBalance(wallet.getBalance().subtract(amount));
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
            }

        } else {
            throw new IllegalArgumentException("Unsupported transaction status.");
        }

        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }
}
