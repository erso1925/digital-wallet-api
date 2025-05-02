-- Seed Customer 1 with a usable wallet
INSERT INTO customers (id, name, surname, tckn, role)
VALUES (999, 'Ersen', 'Pamuk', '12345678901', 'CUSTOMER');

INSERT INTO wallets (id, customer_id, wallet_name, currency, active_for_shopping, active_for_withdraw, balance, usable_balance)
VALUES (199, 999, 'Test Wallet', 'TRY', true, true, 100.00, 100.00);

-- Seed Customer 2 with a disabled wallet
INSERT INTO customers (id, name, surname, tckn, role)
VALUES (1000, 'Other', 'User', '98765432100', 'CUSTOMER');

INSERT INTO wallets (id, customer_id, wallet_name, currency, active_for_shopping, active_for_withdraw, balance, usable_balance)
VALUES (299, 1000, 'Other Wallet', 'USD', false, false, 200.00, 200.00);
