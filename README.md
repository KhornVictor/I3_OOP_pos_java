# POS-System

A Point of Sale (POS) System developed in Java using Object-Oriented Programming principles.

## Folder Structure

```
POS-System/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── pos/
│   │   │           │
│   │   │           ├── Main.java
│   │   │           │
│   │   │           ├── component
│   │   │           │   ├── layout
│   │   │           │   └── component
│   │   │           │
│   │   │           ├── config/
│   │   │           │   └── DatabaseConfig.java
│   │   │           │
│   │   │           ├── db/
│   │   │           │   ├── DBConnection.java
│   │   │           │   └── DBInitializer.java
│   │   │           │
│   │   │           ├── model/
│   │   │           │   ├── Product.java
│   │   │           │   ├── User.java
│   │   │           │   ├── Order.java
│   │   │           │   └── OrderItem.java
│   │   │           │
│   │   │           ├── dao/
│   │   │           │   ├── ProductDAO.java
│   │   │           │   ├── UserDAO.java
│   │   │           │   ├── OrderDAO.java
│   │   │           │   └── ReportDAO.java
│   │   │           │
│   │   │           ├── service/
│   │   │           │   ├── ProductService.java
│   │   │           │   ├── AuthService.java
│   │   │           │   └── OrderService.java
│   │   │           │
│   │   │           ├── controller/
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── POSController.java
│   │   │           │   └── ProductController.java
│   │   │           │
│   │   │           ├── view/
│   │   │           │   ├── login/
│   │   │           │   │   └── LoginFrame.java
│   │   │           │   │
│   │   │           │   ├── dashboard/
│   │   │           │   │   └── DashboardFrame.java
│   │   │           │   │
│   │   │           │   ├── pos/
│   │   │           │   │   ├── POSFrame.java
│   │   │           │   │   └── CartPanel.java
│   │   │           │   │
│   │   │           │   ├── product/
│   │   │           │   │   └── ProductManagementFrame.java
│   │   │           │   │
│   │   │           │   └── report/
│   │   │           │       └── SalesReportFrame.java
│   │   │           │
│   │   │           ├── util/
│   │   │           │   ├── DateUtil.java
│   │   │           │   ├── CurrencyUtil.java
│   │   │           │   └── ValidationUtil.java
│   │   │           │
│   │   │           └── exception/
│   │   │               └── DataAccessException.java
│   │   │
│   │   └── resources/
│   │       ├── images/
│   │       │   └── logo.png
│   │       ├── sql/
│   │       │   └── schema.sql
│   │       └── config.properties
│   │
│   └── test/
│       └── java/
│           └── com/pos/
│               └── ProductDAOTest.java
│
├── lib/
│   └── mysql-connector-j.jar
│
├── bin/
│   ├── main/
│   │   └── com/pos/...
│   │
│   └── test/
│       └── java/com/pos/...
│
├── README.md
└── .gitignore
```

## Project Structure Overview

### Core Packages

- **config/**: Database and application configuration management
- **db/**: Database connection and initialization logic
- **model/**: Entity classes (Product, User, Order, OrderItem)
- **dao/**: Data Access Objects for database CRUD operations
- **service/**: Business logic layer
- **controller/**: Application controllers handling user interactions
- **view/**: User Interface components organized by feature
- **util/**: Utility classes for common operations
- **exception/**: Custom exception classes

## Getting Started

1. Ensure you have Java installed on your system
2. Download MySQL connector JAR and place it in the `lib/` folder
3. Set up the database using the schema provided in `src/main/resources/sql/schema.sql`
4. Configure database connection in `src/main/resources/config.properties`
5. Compile and run `Main.java` to start the application

## Dependencies

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

- **MySQL Connector J** (JDBC Driver) - for database connectivity

## POS System Data Flow

1. **User Interface Layer**
   - The user interacts with the POS system (e.g., scanning a product, entering payment).
   
2. **Controller Layer**
   - Receives the user input and processes the request.
   - Validates input and determines the appropriate action.

3. **Service Layer**
   - Contains business logic.
   - The controller calls the service layer to perform operations like processing a sale, applying discounts, etc.

4. **Data Access Object (DAO) Layer**
   - The service layer calls the DAO to interact with the database.
   - The DAO handles data retrieval and updates using SQL queries or ORM tools.

5. **Database**
   - The database stores all the data (e.g., product information, user details, transactions).

6. **Data Return Flow**
   - After the DAO fetches or updates data, it maps the data back to model objects.
   - The service layer returns the processed data to the controller.
   - The controller then sends the response back to the user interface.

## Diagram Overview


```
User Interface Layer  
   ↓  
Controller Layer  
   ↓  
Service Layer  
   ↓  
DAO Layer  
   ↓  
Database  
   ↓  
DAO Layer  
   ↓  
Service Layer  
   ↓  
Controller Layer  
   ↓  
User Interface Layer
```