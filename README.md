# Bank Account

## Description

This project is an application for managing bank accounts, users and transactions. It allows to create and manage bank accounts, perform transactions and manage user information through a set of RESTful services and controllers.

This project is part of Sofka Technologies' DEV INTERMEDIATE training.

## Database Table Relationships

The application uses two main entities: `User` and `BankAccount`. Below are the details of the database tables and their relationships.

### 1. **Users Table**

This table stores information about the users of the system.

| Column Name        | Type          | Description                                      |
|--------------------|---------------|--------------------------------------------------|
| `idUser`           | BIGINT        | Unique identifier for the user (Primary Key).    |
| `identificationNumber` | VARCHAR(255) | Unique identification number for the user.      |
| `name`             | VARCHAR(255)  | Name of the user.                               |
| `email`            | VARCHAR(255)  | Unique email address of the user (Unique).       |
| `password`         | VARCHAR(255)  | Password of the user.                           |

### 2. **BankAccount Table**

This table stores information about the bank accounts associated with users.

| Column Name        | Type          | Description                                      |
|--------------------|---------------|--------------------------------------------------|
| `idAccount`        | BIGINT        | Unique identifier for the bank account (Primary Key). |
| `numberAccount`    | VARCHAR(255)  | Unique account number for the bank account.      |
| `balance`          | DOUBLE        | Current balance in the bank account.            |
| `type`             | VARCHAR(50)   | Type of the bank account (e.g., Savings, Checking). |
| `idUser`           | BIGINT        | Foreign key referencing `idUser` in the `users` table (Many-to-One relationship). |

### 3. **Relationship Between Users and BankAccount**

- The `BankAccount` entity has a **Many-to-One** relationship with the `User` entity.
- Each user can have multiple bank accounts, but each bank account is associated with exactly one user.

#### Entity Relationship Diagram (ERD)

- **User (1) --- (Many) BankAccount**

This means that a user can have multiple bank accounts, but each bank account can only be associated with one user.

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


# Spring Security with JWT and AAA Protocol Configuration

This document describes the configuration and files required to implement Spring Security using JWT and the Authentication, Authorization, and Auditing (AAA) protocol.

## Created Files and Their Functionality

### 1. **SecurityConfig.java**

This file contains the main security configuration for the application, setting up Spring Security with JWT and the AAA protocol. Below are its key components:

- **`SecurityConfig` class**: Defines the security configuration for the application.
- **`securityFilterChain(HttpSecurity http)` method**: Configures security filters, allowing routes in the whitelist (like `/h2-console/**` and `/api/auth/**`) and setting up JWT-based authentication.
- **`authenticationManager()` method**: Creates an `AuthenticationManager` to handle authentication.
- **`passwordEncoder()` method**: Defines a password encoder using `BCryptPasswordEncoder`.

### 2. **JwtFilter.java**

This filter is responsible for intercepting incoming requests to validate the JWT in the authorization header. If the JWT is valid, it allows user authentication within the security context.

- **`JwtFilter` class**: Extends `OncePerRequestFilter` and validates the JWT in incoming requests.
- **`doFilterInternal()` method**: Extracts the JWT from the authorization header, validates it, and if valid, loads the corresponding user into the security context.

### 3. **UserDetail.java**

This class extends Spring Security's `User` and is used to customize the authenticated user's details, such as the username, password, and authorities.

- **`UserDetail` class**: Represents the customized details of a user.

### 4. **AuthController.java**

This controller handles authentication requests. The entry point is `/api/auth/login`, where users submit their credentials and receive a JWT token if authentication is successful.

- **`login()` method**: Authenticates the user using the provided credentials and returns a JWT token.

### 5. **AuthRequestDTO.java**

This Data Transfer Object (DTO) represents the authentication request, containing the user's email and password.

- **`AuthRequestDTO` class**: Contains the `email` and `password` fields for user authentication.

### 6. **AuthResponseDTO.java**

This DTO contains the authentication response, which includes the JWT token generated after successful authentication.

- **`AuthResponseDTO` class**: Contains the `token` field that represents the JWT.

### 7. **AuthService.java**

This service handles the authentication logic, including validating the user's credentials and generating the JWT token.

- **`authenticate()` method**: Validates the user's credentials, generates a JWT token if authentication is successful, and returns it in an `AuthResponseDTO` object.

### 8. **CustomUserDetailsService.java**

This service is responsible for loading user details from the database. It uses a user repository to find a user by their email and return a customized `UserDetails` object.

- **`loadUserByUsername()` method**: Loads a user from the database based on the email and converts it into a `UserDetail` with the necessary credentials.

### 9. **JwtUtil.java**

This component manages the creation, validation, and decoding of JWT tokens.

- **`generateToken()` method**: Generates a JWT token using the user's email.
- **`isTokenValid()` method**: Validates whether a JWT is valid.
- **`getEmailByUser()` method**: Extracts the user's email from the JWT.

## Spring Security Configuration

The security configuration uses a JWT-based authentication pattern, meaning that after successful authentication, the server generates a JWT token that the client must include in subsequent requests in the `Authorization` header as a Bearer token.

### Key Steps:

1. **User Registration and Authentication**:
    - Users can register via the `/api/users/register` route and authenticate their credentials via `/api/auth/login`.
    - The authentication service validates the credentials and returns a JWT token if successful.

2. **Route Protection**:
    - Public routes are whitelisted (like `/h2-console/**` and `/api/auth/**`), while other routes require authentication.
    - The `JwtFilter` intercepts the requests and validates the JWT token before allowing access.

3. **AAA Protocol (Authentication, Authorization, Auditing)**:
    - **Authentication**: Handled through the login process and the validation of the JWT token.
    - **Authorization**: Only authenticated users can access protected routes.
    - **Auditing**: Additional logging or implementations can be added to audit access and activities in the system.

## Security configuration for Testing

To perform unit and integration tests in the security context, a custom security configuration for tests has been created. This configuration allows all routes to be accessible without authentication, facilitating testing.

### 1. **TestSecurityConfig.java**

This file configures a custom security filter for tests, disabling CSRF protection and allowing all requests without authentication. This makes it easier to perform tests without handling authentication in each request.

## Additional Considerations

- **H2 Console**: The H2 console access is enabled in the development environment by including it in the whitelist.
- **Production Security**: Ensure that a more secure secret is configured for generating JWT tokens in a production environment, and perform adequate security testing.


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

# Unit Tests for Services

This files describes the unit tests implemented for the methods of the `BankAccountService`, `UserService` y `TransactionService` in the `BankAccount` application. The tests are executed using `JUnit`, `Mockito`, and `MockMvc` to validate that the service methods work as expected.

- [BankAccountServiceImplTest.java](src/test/java/com/bankAccount/bankAccount/services/BankAccountServiceImplTest.java)
- [TransactionServiceImplTest.java](src/test/java/com/bankAccount/bankAccount/services/TransactionServiceImplTest.java)
- [UserServiceImplTest.java](src/test/java/com/bankAccount/bankAccount/services/UserServiceImplTest.java)

## Tested Methods

### 1. **Get All Accounts by User**

**Method**: `List<BankAccount> getAllAccountsByUser(long idUser)`

- It tests that the service correctly retrieves all accounts for a user.
- A mock `bankAccountRepository.findAll()` is used to return a list of accounts.
- The result is validated to ensure the correct accounts are returned.

### 2. **Get Balance by Account**

**Method**: `BankAccountResponseDTO getBalanceByAccount(long idAccount)`

- It tests that the service correctly retrieves the balance for a given account.
- The method simulates an existing account in the repository and checks if the correct balance is returned in the response.
- It also verifies the response status (`success` and `message`) when the account is not found.

### 3. **Create Account**

**Method**: `BankAccountResponseDTO createAccount(long idUser, BankAccount account)`

- It tests that the service correctly creates a new bank account for a user.
- The method simulates a successful account creation when the user is found in the repository.
- It also checks that the correct success message and account data are returned in the response.
- It verifies the response when the user is not found (failure scenario).

### 4. **Update Account**

**Method**: `BankAccountResponseDTO updateAccount(long idAccount, BankAccount account)`

- It tests that the service correctly updates an existing account's details.
- The method simulates an existing account in the repository and updates the account's fields.
- It checks for a successful update response as well as failure when the account does not exist.

### 5. **Delete Account**

**Method**: `BankAccountResponseDTO deleteAccount(long idAccount)`

- It tests that the service correctly deletes an account.
- The method checks for the response when the account is found and when it is not found (failure scenario).
- It also validates the success message when the account is deleted successfully.

### 6. **Deposit Money**

**Method**: `BankAccountResponseDTO depositMoney(long idAccount, double amount)`

- It tests that the service correctly processes a deposit transaction.
- The method checks for correct balance updates after a deposit and verifies the success response.
- It also tests the failure response for invalid amounts or when the account is not found.

### 7. **Withdraw Money**

**Method**: `BankAccountResponseDTO withdrawMoney(long idAccount, double amount)`

**Endpoint**: `POST /api/accounts/withdraw/{id}`

- It tests that the service correctly processes a withdrawal transaction.
- The method checks for balance updates after a withdrawal and verifies the success response.
- It tests failure scenarios, such as insufficient funds or when the account is not found.

### 8. **Add Transaction**

**Method**: `void addTransaction(String message, double amount)`

- It tests that the service correctly adds a new transaction to the list.
- The method verifies that the transaction is added with the correct message, amount, and success status, and checks that the date is not null.

### 9. **Get All Transactions**

**Method**: `List<TransactionResponseDTO> getAllTransactions()`

- It tests that the service correctly retrieves all transactions.
- The method checks that the correct number of transactions is returned and that the messages correspond to the added transactions.

### 10. **Get Transactions List**

**Method**: `List<String> getTransactionsList()`

- It tests that the service correctly retrieves a list of transaction messages.
- The method checks that the list contains only the messages from the transactions and verifies their correctness.

### 11. **Get All Users**

**Method**: `getAllUsers()`

- It tests that the service correctly returns a list of all users.
- The response from `userRepository.findAll()` is simulated to return a list of users.
- The response is validated to ensure the correct number of users and their properties.

### 12. **Create User**

**Method**: `createUser(User user)`

- **Success Case**: Verifies that the user is created successfully and returns a success message with the created user.
- **Failure Case**: Simulates a failure (e.g., database error) and ensures that the correct error message is returned.

### 13. **Update User**

**Method**: `updateUser(long idUser, User user)`

- **Success Case**: Verifies that an existing user is updated successfully and returns the updated user.
- **User Not Found Case**: Ensures that when the user is not found, a "user not found" message is returned.

### 14. **Delete User**

**Method**: `deleteUser(long idUser)`

- **Success Case**: Verifies that an existing user is deleted successfully and returns a success message.
- **User Not Found Case**: Ensures that when the user is not found, a "user not found" message is returned.
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