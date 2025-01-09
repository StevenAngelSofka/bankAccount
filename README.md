# Bank Account

## Description

This project is an application for managing bank accounts, users and transactions. It allows to create and manage bank accounts, perform transactions and manage user information through a set of RESTful services and controllers.

This project is part of Sofka Technologies' DEV INTERMEDIATE training.

## Project Structure

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

## Installation

1. Clone this repository on your local machine:

   ``git clone https://github.com/StevenAngelSofka/bankAccount```.

Navigate to the project directory:

    ```cd bank-account```.

3. Install the necessary dependencies:

   ```mvn install```

## Usage

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