# banking-system-demo
Demo banking system implemented in Java using JDBC for database access and MySQL as backend.

## Infrastructure

- Java 17+
- MySQL
- JDBC
- Maven
- Visual Studio Codium + Red Hat Java extension
- Git & GitHub

## Project Structure
```
banking-system-demo/
├── src/
│ └── main/java/com/davidelatina/bankingdemo/
│     ├── controller/        // Controller for managing application flow and state       
│     │   └── BankController.java
│     ├── model/             // Application model (business logic, entities, services)
│     │   ├── entity/        // Core business entities
│     │   │   └── Customer.java
│     │   │   └── Account.java
│     │   ├── service/       // Business logic
│     │   │   └── CustomerService.java
│     │   │   └── AccountService.java
│     │   └── util/          // General utilities
│     │       └── SomeCalculator.java
│     ├── dao/               // Data Access Objects (interacting with database)
│     │   ├── util/          // Database connection manager
│     │   │   └── ConnectionManager.java
│     │   ├── CustomerDAO.java
│     │   └── AccountDAO.java
│     ├── view/              // All View-related components
│     │   ├── dto/           // Menu record as Data Transfer Object
│     │   │   └── Menu.java  
│     │   └── impl/          // View implementation
│     │       └── MenuView.java
│     └── main/              // Application entry point 
│         └── Main.java/     
│      
├── .env
├── banking_db_data.sql
├── banking_db_init.sql
├── pom.xml
└── README.md
```
## Prerequisites

- Java 17 or higher
- Maven installed (`mvn -v`)
- MySQL server
- Git

## Setup Instructions

- Clone the repository

- Start a MySQL server

- Run the provided SQL scripts to create and populate the scheme

- Copy .env.example to a .env file, and insert server address and credentials

- Build with ```mvn clean install```

- Run with ```java -jar ./target/banking-system-demo-1.0-SNAPSHOT-jar-with-dependencies.jar```