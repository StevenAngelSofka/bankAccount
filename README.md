# Bank Account

## Description

This project is an application for managing bank accounts, users and transactions. It allows to create and manage bank accounts, perform transactions and manage user information through a set of RESTful services and controllers.

This project is part of Sofka Technologies' DEV INTERMEDIATE training.

# Project Structure

The project is divided into the following layers:

### Entities
Entities represent the main objects of the system, which correspond to the database tables.

- [BankAccount.java](src/main/java/com/bankAccount/bankAccount/entities/BankAccount.java)
- [User.java](src/main/java/com/bankAccount/bankAccount/entities/User.java)

### Repositories
Repositories provide the database access interface for system entities.

- [BankAccountRepository.java](src/main/java/com/bankAccount/bankAccount/repository/BankAccountRepository.java)
- [UserRepository.java](src/main/java/com/bankAccount/bankAccount/repository/UserRepository.java)

### DTO (Data Transfer Objects)
DTOs are objects used to transfer data between application layers. These DTOs are used to send a standard response for operations related to users, bank accounts and transactions.

- [BankAccountResponseDTO.java](src/main/java/com/bankAccount/bankAccount/dto/bankAccount/BankAccountResponseDTO.java)
- [TransactionResponseDTO.java](src/main/java/com/bankAccount/bankAccount/dto/transaction/TransactionResponseDTO.java)
- [UserResponseDTO.java](src/main/java/com/bankAccount/bankAccount/dto/user/UserResponseDTO.java)

### Services
Services contain the business logic to handle operations on entities.

#### Bank Account

- [BankAccountService.java](src/main/java/com/bankAccount/bankAccount/services/bankAccount/BankAccountService.java)
- [BankAccountServiceImpl.java](src/main/java/com/bankAccount/bankAccount/services/bankAccount/BankAccountServiceImpl.java)

#### Transaction

- [TransactionService.java](src/main/java/com/bankAccount/bankAccount/services/transaction/TransactionService.java)
- [TransactionServiceImpl.java](src/main/java/com/bankAccount/bankAccount/services/transaction/TransactionServiceImpl.java)

#### Users

- [UserService.java](src/main/java/com/bankAccount/bankAccount/services/user/UserService.java)
- [UserServiceImpl.java](src/main/java/com/bankAccount/bankAccount/services/user/UserServiceImpl.java)

### Drivers
Controllers handle HTTP requests and expose RESTful APIs.

- [BankAccountController.java](src/main/java/com/bankAccount/bankAccount/controllers/bankAccount/BankAccountController.java)
- [TransactionController.java](src/main/java/com/bankAccount/bankAccount/controllers/transaction/TransactionController.java)
- [UserController.java](src/main/java/com/bankAccount/bankAccount/controllers/user/UserController.java)


# Unit Tests for Controllers

This file describes the unit tests implemented for the controllers of the `BankAccount` application. The tests are executed using `JUnit`, `Mockito`, and `MockMvc` to validate that the controllers correctly handle the requests and responses from the services.

- [UserControllerTest.java](src/test/java/com/bankAccount/bankAccount/controllers/UserControllerTest.java)
- [TransactionControllerTest.java](src/test/java/com/bankAccount/bankAccount/controllers/TransactionControllerTest.java)
- [TransactionControllerTest.java](src/test/java/com/bankAccount/bankAccount/controllers/TransactionControllerTest.java)

## Tested Endpoints

### 1. **Get All Users**

**Endpoint**: `GET /api/users`

- It tests that the controller correctly returns a list of users.
- The response from the `userService.getAllUsers()` service is simulated with a list of users.
- The response status and user data are validated.

### 2. **Create User**

**Endpoint**: `POST /api/users/create`

- It tests that the controller correctly creates a user.
- The call to the `userService.createUser()` service is simulated, and the success message and created user data are validated.

### 3. **Update User**

**Endpoint**: `PUT /api/users/update/{id}`

- It tests that the controller correctly updates an existing user.
- The response from the `userService.updateUser()` service is simulated, and it is validated that the user has been correctly updated.

### 4. **Delete User**

**Endpoint**: `DELETE /api/users/delete/{id}`

- It tests that the controller correctly deletes a user.
- The call to the `userService.deleteUser()` service is simulated, and the success message is validated.

### 5. **Get Transactions**

**Endpoint**: `GET /api/transactions/get-transactions`

- It tests that the controller correctly returns a list of transactions.
- The response from the `transactionService.getAllTransactions()` service is simulated, and the transactions are validated.

### 6. **Get Transaction List**

**Endpoint**: `GET /api/transactions/get-transactions-list`

- It tests that the controller correctly returns a list of transactions in `String` format.
- The response from the `transactionService.getTransactionsList()` service is simulated, and the list content is validated.

### 7. **Get Accounts by User**

**Endpoint**: `GET /api/accounts/get-accounts-by-user/{id}`

- It tests that the controller correctly returns a list of bank accounts associated with a user.
- The response from the `bankAccountService.getAllAccountsByUser()` service is simulated with two user accounts.
- The response status is validated as `200 OK`, the content type is JSON, and the returned accounts have the correct information.

### 8. **Get Account Balance**

**Endpoint**: `GET /api/accounts/get-balance-by-account/{id}`

- It tests that the controller correctly returns the balance of an account.
- The response from the `bankAccountService.getBalanceByAccount()` service is simulated with a balance value.
- The response status is validated as `200 OK`, the content type is JSON, and the account balance matches the expected value.

### 9. **Create Account**

**Endpoint**: `POST /api/accounts/create/{idUser}`

- It tests that the controller correctly creates a bank account for a user.
- The response from the `bankAccountService.createAccount()` service is simulated with a created bank account.
- The response status is validated as `200 OK`, the content type is JSON, and the created account data matches the submitted data.

### 10. **Deposit Money into Account**

**Endpoint**: `POST /api/accounts/deposit/{id}`

- It tests that the controller correctly makes a deposit into a bank account.
- The response from the `bankAccountService.depositMoney()` service is simulated with a success message.
- The response status is validated as `200 OK`, the content type is JSON, and the message indicates that the deposit was successful.

### 11. **Withdraw Money from Account**

**Endpoint**: `POST /api/accounts/withdraw/{id}`

- It tests that the controller correctly makes a withdrawal from a bank account.
- The response from the `bankAccountService.withdrawMoney()` service is simulated with a success message.
- The response status is validated as `200 OK`, the content type is JSON, and the message indicates that the withdrawal was successful.

### 12. **Update Account**

**Endpoint**: `PUT /api/accounts/update/{id}`

- It tests that the controller correctly updates the data of a bank account.
- The response from the `bankAccountService.updateAccount()` service is simulated with the updated account data.
- The response status is validated as `200 OK`, the content type is JSON, and the updated account data matches the submitted data.

### 13. **Delete Account**

**Endpoint**: `DELETE /api/accounts/delete/{id}`

- It tests that the controller correctly deletes a bank account.
- The response from the `bankAccountService.deleteAccount()` service is simulated with a success message.
- The response status is validated as `200 OK`, the content type is JSON, and the message indicates that the account was successfully deleted.


# Unit Tests for Entities and DTOs

This file describes the unit tests implemented for the entities and DTOs of the `BankAccount` application. The tests are executed using `JUnit` to validate the behavior of the entities and DTOs, ensuring they correctly handle data and basic functionality.

- [UserTest.java](src/test/java/com/bankAccount/bankAccount/entities/UserTest.java)
- [BankAccountTest.java](src/test/java/com/bankAccount/bankAccount/entities/BankAccountTest.java)
- [BankAccountResponseDTOTest.java](src/test/java/com/bankAccount/bankAccount/dto/BankAccountResponseDTOTest.java)
- [TransactionResponseDTOTest.java](src/test/java/com/bankAccount/bankAccount/dto/TransactionResponseDTOTest.java)
- [UserResponseDTOTest.java](src/test/java/com/bankAccount/bankAccount/dto/UserResponseDTOTest.java)

## Unit Tests for Entities

### 1. **User Entity**

**Test Class**: `UserTest.java`

- **Test Method**: `testUserPersistence`
   - It tests that the `User` entity is persisted correctly in the database.
   - The `User` object is saved and its properties are validated, including the auto-generated ID and correct field values.

- **Test Method**: `testUserWithoutId`
   - It verifies that a `User` entity without an ID will have it generated automatically when saved.
   - The ID generation is confirmed and the entity's data is validated.

### 2. **BankAccount Entity**

**Test Class**: `BankAccountTest.java`

- **Test Method**: `testBankAccountPersistence`
   - It tests that the `BankAccount` entity is persisted correctly in the database.
   - The `BankAccount` object is saved and its properties are validated, including correct association with the `User` entity.

- **Test Method**: `testBankAccountWithoutId`
   - It verifies that a `BankAccount` entity without an ID will have it generated automatically when saved.
   - The ID generation is confirmed and the entity's data is validated.

## Unit Tests for DTOs

### 1. **UserResponseDTO**

**Test Class**: `UserResponseDTOTest.java`

- **Test Method**: `testUserResponseDTO`
   - It tests the construction and data assignment of the `UserResponseDTO` object.
   - The `UserResponseDTO` object is tested for correct data mapping and constructor functionality.

### 2. **BankAccountResponseDTO**

**Test Class**: `BankAccountResponseDTOTest.java`

- **Test Method**: `testBankAccountResponseDTO`
   - It tests the construction and data assignment of the `BankAccountResponseDTO` object.
   - The `BankAccountResponseDTO` object is tested for correct data mapping and constructor functionality.

### 3. **TransactionResponseDTO**

**Test Class**: `TransactionResponseDTOTest.java`

- **Test Method**: `testTransactionResponseDTO`
   - It tests the construction and data assignment of the `TransactionResponseDTO` object.
   - The `TransactionResponseDTO` object is tested for correct data mapping and constructor functionality.

---

# Integration Tests for Entities

This file describes the integration tests implemented for the entities of the `BankAccount` application. The tests are executed using `@DataJpaTest` to validate that the entities interact correctly with the database, including persistence and relationships.

- [BankAccountIT.java](src/test/java/com/bankAccount/bankAccount/integration/entities/BankAccountIT.java)
- [UserIT.java](src/test/java/com/bankAccount/bankAccount/integration/entities/UserIT.java)

### 1. **User Entity Integration Test**

**Test Class**: `UserRepositoryTest.java`

- **Test Method**: `testUserPersistence`
   - It tests that a `User` entity is correctly saved and retrieved from the database.
   - The test validates that the `User` is correctly persisted with its properties and relationships.

- **Test Method**: `testUserRetrieval`
   - It tests that a saved `User` can be retrieved from the database using the `UserRepository`.
   - The test ensures that the retrieved `User` matches the one that was saved.

### 2. **BankAccount Entity Integration Test**

**Test Class**: `BankAccountRepositoryTest.java`

- **Test Method**: `testBankAccountPersistence`
   - It tests that a `BankAccount` entity is correctly saved and retrieved from the database.
   - The test validates that the `BankAccount` is correctly persisted with its properties and its relationship with the `User` entity.

- **Test Method**: `testBankAccountRetrieval`
   - It tests that a saved `BankAccount` can be retrieved from the database using the `BankAccountRepository`.
   - The test ensures that the retrieved `BankAccount` matches the one that was saved.


## Test Execution

To run unit tests:

1. **with Gradle**:
   ``` ./gradlew test```
   
# Installation

1. Clone this repository on your local machine:

   ```git clone https://github.com/StevenAngelSofka/bankAccount```.

Navigate to the project directory:

    ```cd bank-account```.

3. Install the necessary dependencies:

   ```mvn install```

# Usage

1. Run the project:

   ```mvn spring-boot:run```

2. Access the API through 

   ```http://localhost:8080```

3. To view the test database, access the H2 console at the following address:

   ```http://localhost:8080/h2-console```

   In the H2 console, use the following settings to connect to the database:
   
   - **JDBC URL**: `jdbc:h2:file:./bankdb`
   - **User Name**: `sa`
   - **Password**: *(leave blank)*