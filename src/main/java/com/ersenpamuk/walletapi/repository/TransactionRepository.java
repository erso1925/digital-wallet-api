package com.ersenpamuk.walletapi.repository;

import com.ersenpamuk.walletapi.entity.Transaction;
import com.ersenpamuk.walletapi.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Retrieves all transactions related to a specific wallet
    List<Transaction> findByWallet(Wallet wallet);
}
