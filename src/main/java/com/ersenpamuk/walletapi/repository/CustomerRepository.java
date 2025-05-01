package com.ersenpamuk.walletapi.repository;

import com.ersenpamuk.walletapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this interface as a Spring Data repository bean
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find a customer by their unique national ID (TCKN)
    Optional<Customer> findByTckn(String tckn);
}
