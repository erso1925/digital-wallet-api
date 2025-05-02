# Digital Wallet API – Case Study

This project implements a **Java Spring Boot** backend API for a digital payment company. It allows users to:
- Create wallets
- Deposit and withdraw funds
- View transaction history
- Approve or deny transactions (for Employee role)

---

## Tech Stack

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security (Basic Auth, InMemory)
- H2 In-Memory Database
- JUnit 5 + Mockito
- Maven

---

## How to Run

```bash
# 1. Clone the repository
git clone https://github.com/erso1925/digital-wallet-api.git
cd digital-wallet-api

# 2. Run the application
./mvnw spring-boot:run

# Or use IntelliJ: Run `DigitalWalletApiApplication.java`
```

```
To import the collection into Postman:

1. Open Postman
2. Click on `Import`
3. Select the file: `digital-wallet-api.postman_collection.json`
4. Run requests directly

```

---
##  Authentication
| Username     | Password   | Role      |
|--------------|------------|-----------|
| employee999  | password   | EMPLOYEE  |
| customer999  | password   | CUSTOMER  |
| customer1000 | password2  | CUSTOMER  |
All requests require Basic Auth.
---

## API Endpoints + Sample JSONs
...

## Usage Guide (Happy Path Walkthrough)

A complete walkthrough is available in the [`USAGE.md`](./USAGE.md) file.

This file includes:
- Authenticated sample requests
- Expected responses
- Transaction flow scenarios
- Final balance validation

## Test Instructions

```
./mvnw test
```

## Project Structure

```
src
├── main
│   ├── java
│   │   └── com.ersenpamuk.walletapi
│   │       ├── config
│   │       ├── controller
│   │       ├── dto/response
│   │       ├── entity
│   │       ├── enums
│   │       ├── exception
│   │       ├── repository
│   │       ├── service/impl
│   │       └── DigitalWalletApiApplication.java
│   └── resources
│       ├── application.properties
│       └── data.sql
└── test
    └── java
        └── com.ersenpamuk.walletapi
            ├── service
            │   ├── WalletServiceTest.java
            │   └── TransactionServiceTest.java
            └── IngDigitalWalletApiApplicationTests.java
```

## Sample Test Data
data.sql contains:

```
customer999 with active TRY wallet
customer1000 with inactive USD wallet
```

## Notes
```
Amounts > 1000 → status: PENDING
Amounts ≤ 1000 → status: APPROVED
Customers can only access their own data
Employees have full access
```

## Author
```
Ersen Pamuk
Backend Developer – Case Study
GitHub: @erso1925
```
