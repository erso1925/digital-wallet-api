package com.ersenpamuk.walletapi.service;

import com.ersenpamuk.walletapi.entity.Customer;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.CurrencyType;

import java.util.List;
import java.util.Optional;

public interface WalletService {

    // Creates a new wallet instance for a customer
    Wallet createWallet(Wallet wallet);

    // Retrieves a wallet by its ID
    Optional<Wallet> findById(Long id);

    // Returns all wallets owned by a customer
    List<Wallet> getWalletsByCustomer(Customer customer);

    // Returns wallets of a customer filtered by currency
    List<Wallet> getWalletsByCustomerAndCurrency(Customer customer, CurrencyType currency);
}
