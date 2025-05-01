package com.ersenpamuk.walletapi.repository;

import com.ersenpamuk.walletapi.entity.Customer;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // Returns all wallets that belong to a given customer
    List<Wallet> findByCustomer(Customer customer);

    // Returns wallets filtered by customer and currency type
    List<Wallet> findByCustomerAndCurrency(Customer customer, CurrencyType currency);
}
