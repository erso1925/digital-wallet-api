package com.ersenpamuk.walletapi.controller;

import com.ersenpamuk.walletapi.dto.CreateWalletRequest;
import com.ersenpamuk.walletapi.entity.Customer;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.CurrencyType;
import com.ersenpamuk.walletapi.service.CustomerService;
import com.ersenpamuk.walletapi.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/wallet")
public class WalletController {

    private final WalletService walletService;
    private final CustomerService customerService;

    @Autowired
    public WalletController(WalletService walletService, CustomerService customerService) {
        this.walletService = walletService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> createWallet(@RequestParam Long customerId,
                                          @Valid @RequestBody CreateWalletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE"));

        // Authorization: CUSTOMER can only create wallets for themselves
        if (!isEmployee && !username.equals("customer" + customerId)) {
            throw new AccessDeniedException("Customers can only operate on their own wallets.");
        }

        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setWalletName(request.getWalletName());
        wallet.setCurrency(request.getCurrency());
        wallet.setActiveForShopping(request.getActiveForShopping());
        wallet.setActiveForWithdraw(request.getActiveForWithdraw());

        Wallet savedWallet = walletService.createWallet(wallet);
        return ResponseEntity.ok(savedWallet);
    }

    @GetMapping
    public ResponseEntity<?> listWallets(@RequestParam Long customerId,
                                         @RequestParam(required = false) String currency) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE"));

        // Authorization: CUSTOMER can only list their own wallets
        if (!isEmployee && !username.equals("customer" + customerId)) {
            throw new AccessDeniedException("Customers can only list their own wallets.");
        }

        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        List<Wallet> wallets;

        if (currency != null) {
            try {
                // Try to parse the currency filter
                CurrencyType currencyType = CurrencyType.valueOf(currency.toUpperCase());
                wallets = walletService.getWalletsByCustomerAndCurrency(customer, currencyType);

                if (wallets.isEmpty()) {
                    return ResponseEntity.status(404)
                            .body("No wallet found for customer with currency: " + currencyType);
                }

            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body("Invalid currency type: " + currency);
            }

        } else {
            wallets = walletService.getWalletsByCustomer(customer);

            if (wallets.isEmpty()) {
                return ResponseEntity.status(404)
                        .body("No wallet found for customer with ID: " + customerId);
            }
        }

        return ResponseEntity.ok(wallets);
    }
}
