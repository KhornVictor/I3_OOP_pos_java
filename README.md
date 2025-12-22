## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


POS-Project/
│
├── src/
│   └── com/
│       └── pos/
│           ├── Main.java                 // Entry point
│           │
│           ├── model/
│           │   ├── Product.java
│           │   ├── Sale.java
│           │   └── Customer.java
│           │
│           ├── view/
│           │   ├── LoginFrame.java
│           │   ├── MainDashboard.java
│           │   ├── ProductPanel.java
│           │   └── SaleFrame.java
│           │
│           ├── controller/
│           │   ├── ProductController.java
│           │   └── SaleController.java
│           │
│           └── database/
│               ├── DBConnection.java      // Handles database connection
│               ├── ProductDAO.java
│               └── SaleDAO.java
│
├── lib/
│   └── mysql-connector-java-8.0.xx.jar   // Your JDBC driver (download & place here)
│
├── resources/
│   ├── db.properties                     // Store DB URL, username, password
│   └── icons/                            // Optional: images for buttons, etc.
│       ├── logo.png
│       └── cart.png
│
├── sql/
│   └── pos_schema.sql                    // Your database creation script
│
├── README.txt
└── run.bat  (or run.sh for Linux/Mac)   // Optional: script to run the app