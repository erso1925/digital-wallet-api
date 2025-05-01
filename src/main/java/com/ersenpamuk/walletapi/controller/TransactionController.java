package com.ersenpamuk.walletapi.controller;

import com.ersenpamuk.walletapi.dto.DepositRequest;
import com.ersenpamuk.walletapi.dto.UpdateTransactionStatusRequest;
import com.ersenpamuk.walletapi.dto.WithdrawRequest;
import com.ersenpamuk.walletapi.dto.response.TransactionResponse;
import com.ersenpamuk.walletapi.entity.Transaction;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.TransactionStatus;
import com.ersenpamuk.walletapi.service.TransactionService;
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
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final WalletService walletService;

    @Autowired
    public TransactionController(TransactionService transactionService, WalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody DepositRequest request) {

        Wallet wallet = walletService.findById(request.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found with id: " + request.getWalletId()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE"));

        // Authorization: CUSTOMER can only deposit to their own wallets
        if (!isEmployee && !username.equals("customer" + wallet.getCustomer().getId())) {
            throw new AccessDeniedException("Customers can only deposit into their own wallets.");
        }

        Transaction transaction = transactionService.deposit(
                wallet,
                request.getAmount(),
                request.getSource(),
                request.getSourceType()
        );

        TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                wallet.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getOppositePartyType(),
                transaction.getOppositeParty(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WithdrawRequest request) {

        Wallet wallet = walletService.findById(request.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found with id: " + request.getWalletId()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE"));

        // Authorization: CUSTOMER can only withdraw from their own wallets
        if (!isEmployee && !username.equals("customer" + wallet.getCustomer().getId())) {
            throw new AccessDeniedException("Customers can only withdraw from their own wallets.");
        }

        Transaction transaction = transactionService.withdraw(
                wallet,
                request.getAmount(),
                request.getDestination(),
                request.getDestinationType()
        );

        TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                wallet.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getOppositePartyType(),
                transaction.getOppositeParty(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> listTransactions(@RequestParam Long walletId) {

        Wallet wallet = walletService.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found with id: " + walletId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE"));

        // Authorization: CUSTOMER can only list their own wallet transactions
        if (!isEmployee && !username.equals("customer" + wallet.getCustomer().getId())) {
            throw new AccessDeniedException("Customers can only view their own transactions.");
        }

        List<Transaction> transactions = transactionService.getTransactionsByWallet(wallet);

        if (transactions.isEmpty()) {
            return ResponseEntity.status(404).body("No transactions found for wallet ID: " + walletId);
        }

        List<TransactionResponse> responseList = transactions.stream().map(t -> new TransactionResponse(
                t.getId(),
                wallet.getId(),
                t.getAmount(),
                t.getType(),
                t.getOppositePartyType(),
                t.getOppositeParty(),
                t.getStatus(),
                t.getCreatedAt()
        )).toList();

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/approve")
    public ResponseEntity<?> updateTransactionStatus(@Valid @RequestBody UpdateTransactionStatusRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE"));

        // Only EMPLOYEE role can approve or deny transactions
        if (!isEmployee) {
            throw new AccessDeniedException("Only EMPLOYEE users can approve or deny transactions.");
        }

        Transaction transaction = transactionService.findById(request.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + request.getTransactionId()));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            return ResponseEntity.badRequest().body("Only PENDING transactions can be updated.");
        }

        Transaction updatedTransaction = transactionService.updateStatus(transaction, request.getNewStatus());

        TransactionResponse response = new TransactionResponse(
                updatedTransaction.getId(),
                updatedTransaction.getWallet().getId(),
                updatedTransaction.getAmount(),
                updatedTransaction.getType(),
                updatedTransaction.getOppositePartyType(),
                updatedTransaction.getOppositeParty(),
                updatedTransaction.getStatus(),
                updatedTransaction.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }
}
