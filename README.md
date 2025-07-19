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

banking-system-demo/

├── src/

│ ├── main/java/com/davidelatina/bankingdemo/

│ │ ├── dao/

│ │ ├── main/

│ │ ├── model/

│ │ └── util/

├── .env

├── banking_db_data.sql

├── banking_db_init.sql

├── pom.xml

└── README.md

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