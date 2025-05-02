# Digital Wallet API – Usage Guide (Happy Path)
This guide provides a step-by-step walkthrough to test the **core functionality** of the Digital Wallet API using either Postman or curl.  
All scenarios assume authentication is handled via **Basic Auth** (credentials listed below).
---
##  Authentication
| Username     | Password   | Role      |
|--------------|------------|-----------|
| employee999  | password   | EMPLOYEE  |
| customer999  | password   | CUSTOMER  |
| customer1000 | password2  | CUSTOMER  |
All requests require Basic Auth.
---
##  Step-by-Step Walkthrough
### Step 1 – List Existing Wallets
Get all wallets of a customer.
**Endpoint:**
GET /customers/wallet?customerId=999

**Headers:**
Authorization: Basic Auth (customer999:password)

**Expected Response:**
```json
[
    {
        "id": 199,
        "customer": {
            "id": 999,
            "name": "Ersen",
            "surname": "Pamuk",
            "tckn": "12345678901",
            "role": "CUSTOMER"
        },
        "walletName": "Test Wallet",
        "currency": "TRY",
        "activeForShopping": true,
        "activeForWithdraw": true,
        "balance": 100.00,
        "usableBalance": 100.00
    }
]
```
### Step 2 – Create New Wallet
Create a second wallet (EUR) for the same customer.
**Endpoint:**
POST customers/wallet?customerId=999

**Headers:**
Authorization: Basic Auth (customer999:password)
Content-Type: application/json
**Request Body:**
```json
{
  "walletName": "Euro Wallet",
  "currency": "EUR",
  "activeForShopping": true,
  "activeForWithdraw": true
}
```
**Expected Response:**
```json
{
  "id": 1,
  "customer": {
    "id": 999,
    "name": "Ersen",
    "surname": "Pamuk",
    "tckn": "12345678901",
    "role": "CUSTOMER"
  },
  "walletName": "Euro Wallet",
  "currency": "EUR",
  "activeForShopping": true,
  "activeForWithdraw": true,
  "balance": 0,
  "usableBalance": 0
}
```

### Step 3 – Deposit Under 1000₺ (→ APPROVED)
Deposit a small amount that should be approved immediately.

**Endpoint:**
POST /transactions/deposit

**Headers:**
Authorization: Basic Auth (customer999:password)  
Content-Type: application/json

**Request Body:**
```json
{
  "walletId": 199,
  "amount": 600,
  "source": "TR1234567890123456",
  "sourceType": "IBAN"
}
```

**Expected Response:**
```json
{
  "id": 1,
  "walletId": 199,
  "amount": 600,
  "type": "DEPOSIT",
  "oppositePartyType": "IBAN",
  "oppositeParty": "TR1234567890123456",
  "status": "APPROVED",
  "createdAt": "2025-05-02T02:40:21.5786508"
}
```

### Step 4 – Withdraw Under 1000₺ (→ APPROVED)
Withdraw a small amount that should be approved immediately.

**Endpoint:**
POST /transactions/withdraw

**Headers:**
Authorization: Basic Auth (customer999:password)  
Content-Type: application/json

**Request Body:**
```json
{
  "walletId": 199,
  "amount": 100,
  "destination": "TR1234567890123456",
  "destinationType": "IBAN"
}
```

**Expected Response:**
```json
{
  "id": 2,
  "walletId": 999,
  "amount": 100,
  "type": "WITHDRAW",
  "oppositePartyType": "IBAN",
  "oppositeParty": "TR1234567890123456",
  "status": "APPROVED",
  "createdAt": "2025-05-02T02:42:33.7615556"
}
```
This transaction will:

Decrease both balance and usableBalance by 100

Be marked as APPROVED immediately

### Step 5 – Deposit Over 1000₺ (→ PENDING)
Deposit a high amount that will be marked as PENDING and require approval by an employee.

**Endpoint:**
POST /transactions/deposit

**Headers:**
Authorization: Basic Auth (customer999:password)  
Content-Type: application/json

**Request Body:** (amount > 1000)
```json
{
  "walletId": 199,
  "amount": 1700,
  "source": "TR1234567890123456",
  "sourceType": "IBAN"
}
```

**Expected Response:**
```json
{
  "id": 3,
  "walletId": 199,
  "amount": 1700,
  "type": "DEPOSIT",
  "oppositePartyType": "IBAN",
  "oppositeParty": "TR1234567890123456",
  "status": "PENDING",
  "createdAt": "2025-05-02T08:33:03.2937692"
}
```

- Status: `PENDING`
- `balance` increases
- `usableBalance` remains unchanged
- Transaction is not yet spendable

**List Wallet and Current Balance:**
```json
{
  "id": 199,
  "customer": {
    "id": 999,
    "name": "Ersen",
    "surname": "Pamuk",
    "tckn": "12345678901",
    "role": "CUSTOMER"
  },
  "walletName": "Test Wallet",
  "currency": "TRY",
  "activeForShopping": true,
  "activeForWithdraw": true,
  "balance": 2300.00,
  "usableBalance": 600.00
}
```

### Step 6 – Approve Pending Transaction (Employee Only)
Approve the previously pending 1700₺ deposit as an employee.

**Step 6.1: Identify the pending transaction ID**

**Endpoint:**
GET /transactions?walletId=199

**Headers:**
Authorization: Basic Auth (employee999:password)

**Expected Response:**
```json
[
{
"id": 1,
"walletId": 199,
"amount": 600.00,
"type": "DEPOSIT",
"oppositePartyType": "IBAN",
"oppositeParty": "TR1234567890123456",
"status": "APPROVED",
"createdAt": "2025-05-02T08:40:47.671926"
},
{
"id": 2,
"walletId": 199,
"amount": 100.00,
"type": "WITHDRAW",
"oppositePartyType": "IBAN",
"oppositeParty": "TR1234567890123456",
"status": "APPROVED",
"createdAt": "2025-05-02T08:41:35.83828"
},
{
"id": 3,
"walletId": 199,
"amount": 1700.00,
"type": "DEPOSIT",
"oppositePartyType": "IBAN",
"oppositeParty": "TR1234567890123456",
"status": "PENDING",
"createdAt": "2025-05-02T08:42:22.973783"
}
]
```

**Step 6.2: Approve the transaction**

**Endpoint:**
POST /transactions/approve

**Headers:**
Authorization: Basic Auth (employee999:password)  
Content-Type: application/json

**Request Body:**
```json
{
  "transactionId": 3,
  "newStatus": "APPROVED"
}
```
**Expected Response:**
```json
{
"id": 3,
"walletId": 199,
"amount": 1700.00,
"type": "DEPOSIT",
"oppositePartyType": "IBAN",
"oppositeParty": "TR1234567890123456",
"status": "APPROVED",
"createdAt": "2025-05-02T08:42:22.973783"
}
```

- Status: APPROVED
- usableBalance increases
- balance remains the same
- Funds are now fully spendable

### Step 7 – Final Transaction List

List all transactions for the wallet to confirm the end result.

**Endpoint:**
GET /transactions?walletId=199

**Headers:**
Authorization: Basic Auth (customer999:password)

**Expected Response:**
```json
[
  {
    "id": 1,
    "walletId": 199,
    "amount": 600.00,
    "type": "DEPOSIT",
    "status": "APPROVED"
  },
  {
    "id": 2,
    "walletId": 199,
    "amount": 100.00,
    "type": "WITHDRAW",
    "status": "APPROVED"
  },
  {
    "id": 3,
    "walletId": 199,
    "amount": 1700.00,
    "type": "DEPOSIT",
    "status": "APPROVED"
  }
]
```

### Step 8 – Final Balance Check

Verify that the wallet reflects all approved transactions correctly. This time filter it with currency.

**Endpoint:**
GET /customers/wallet?customerId=999&currency=TRY

**Headers:**
Authorization: Basic Auth (customer999:password)

**Expected Response:**
```json
[
    {
        "id": 199,
        "customer": {
            "id": 999,
            "name": "Ersen",
            "surname": "Pamuk",
            "tckn": "12345678901",
            "role": "CUSTOMER"
        },
        "walletName": "Test Wallet",
        "currency": "TRY",
        "activeForShopping": true,
        "activeForWithdraw": true,
        "balance": 2300.00,
        "usableBalance": 2300.00
    }
]
```

---

## Additional Functionalities (Available in Code & Postman)

This walkthrough focuses on the main user flow.  
However, the project also implements many other functionalities that can be explored via the provided Postman collection:

- Wallet creation and listing with currency filter  
- Deposit and withdraw transaction status logic (`PENDING` / `APPROVED` / `DENIED`)  
- Transaction approval & denial (employee-only)  
- Wallet configuration enforcement (withdraw & shopping flags)  
- Transaction status updates and balance reconciliation  
- Enum validations (CurrencyType, TransactionType, etc.)  
- Field-level input validations via annotations  
- Centralized error handling via `GlobalExceptionHandler`  
- Role-based authentication and access control using Spring Security  

To explore these features, please import and use:  
`digital-wallet-api.postman_collection.json`


