package com.ersenpamuk.walletapi.service.impl;

import com.ersenpamuk.walletapi.entity.Customer;
import com.ersenpamuk.walletapi.repository.CustomerRepository;
import com.ersenpamuk.walletapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Marks this class as a Spring-managed service component
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired // Injects the required repository dependency
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByTckn(String tckn) {
        return customerRepository.findByTckn(tckn);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
