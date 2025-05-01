package com.ersenpamuk.walletapi.service;

import com.ersenpamuk.walletapi.entity.Customer;
import com.ersenpamuk.walletapi.entity.Wallet;
import com.ersenpamuk.walletapi.enums.CurrencyType;
import com.ersenpamuk.walletapi.repository.WalletRepository;
import com.ersenpamuk.walletapi.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Test
    public void shouldCreateWalletSuccessfully() {
        // Arrange
        WalletRepository walletRepository = mock(WalletRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository);

        Customer customer = new Customer();
        customer.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setWalletName("Test Wallet");
        wallet.setCurrency(CurrencyType.TRY);
        wallet.setActiveForShopping(true);
        wallet.setActiveForWithdraw(true);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsableBalance(BigDecimal.ZERO);

        when(walletRepository.save(Mockito.any(Wallet.class))).thenReturn(wallet);

        // Act
        Wallet createdWallet = walletService.createWallet(wallet);

        // Assert
        assertEquals("Test Wallet", createdWallet.getWalletName());
        assertEquals(CurrencyType.TRY, createdWallet.getCurrency());
        verify(walletRepository, times(1)).save(wallet);
    }
}
