package com.ersenpamuk.walletapi.service;

import com.ersenpamuk.walletapi.entity.Customer;

import java.util.Optional;

public interface CustomerService {

    // Retrieves a customer by ID (used in access checks and ownership validation)
    Optional<Customer> findById(Long id);

    // Retrieves a customer by unique TCKN (used in auth scenarios)
    Optional<Customer> findByTckn(String tckn);

    // Saves or updates a customer record (e.g. for test setup or admin ops)
    Customer save(Customer customer);
}
