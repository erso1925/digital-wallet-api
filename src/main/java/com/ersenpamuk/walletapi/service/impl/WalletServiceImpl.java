package com.ersenpamuk.walletapi.service.impl;

import com.ersenpamuk.walletapi.entity.Customer;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.CurrencyType;
import com.ersenpamuk.walletapi.repository.WalletRepository;
import com.ersenpamuk.walletapi.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring-managed service bean
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired // Injects repository dependency via constructor (preferred for testability)
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public List<Wallet> getWalletsByCustomer(Customer customer) {
        return walletRepository.findByCustomer(customer);
    }

    @Override
    public List<Wallet> getWalletsByCustomerAndCurrency(Customer customer, CurrencyType currency) {
        return walletRepository.findByCustomerAndCurrency(customer, currency);
    }
}
