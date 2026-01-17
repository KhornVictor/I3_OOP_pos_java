package main.com.pos.database.migrate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import main.com.pos.database.DBConnection;
import main.com.pos.util.Color;

public class Migrate {
    public void initialize() {
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();

            // Table Address
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS Address (
                        AddressID INT AUTO_INCREMENT PRIMARY KEY,
                        Street VARCHAR(255),
                        City VARCHAR(100),
                        State VARCHAR(100),
                        ZipCode VARCHAR(20),
                        Country VARCHAR(100)
                    ) ENGINE=InnoDB;
                """
            );

            // Table User
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS User (
                        UserID INT AUTO_INCREMENT PRIMARY KEY,
                        Username VARCHAR(50) NOT NULL UNIQUE,
                        Password VARCHAR(255) NOT NULL,
                        Role VARCHAR(20) NOT NULL,
                        Name VARCHAR(100),
                        Email VARCHAR(100),
                        AddressID INT,
                        Image VARCHAR(255),
                        
                        CONSTRAINT fk_user_address
                            FOREIGN KEY (AddressID)
                            REFERENCES Address(AddressID)
                    ) ENGINE=InnoDB;
                """
            );

            // Table Customer
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS Customer (
                        CustomerID INT AUTO_INCREMENT PRIMARY KEY,
                        Name VARCHAR(100) NOT NULL,
                        Email VARCHAR(100),
                        Phone VARCHAR(20),
                        Address TEXT
                    ) ENGINE=InnoDB;
                """
            );

            // Table Category
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS Category (
                        CategoryID INT AUTO_INCREMENT PRIMARY KEY,
                        Name VARCHAR(100) NOT NULL
                    ) ENGINE=InnoDB;
                """
            );

            // Table Product
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS Product (
                        ProductID INT AUTO_INCREMENT PRIMARY KEY,
                        Name VARCHAR(100) NOT NULL,
                        CategoryID INT NOT NULL,
                        Price DECIMAL(10,2) NOT NULL,
                        StockQuantity INT NOT NULL,
                        Image VARCHAR(255),

                        CONSTRAINT fk_product_category
                            FOREIGN KEY (CategoryID)
                            REFERENCES Category(CategoryID)
                    ) ENGINE=InnoDB;
                """
            );

            // Table Sale
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS Sale (
                        SaleID INT AUTO_INCREMENT PRIMARY KEY,
                        SaleDate DATETIME NOT NULL,
                        UserID INT NOT NULL,
                        CustomerID INT,
                        Total DECIMAL(10,2) NOT NULL,
                        Discount DECIMAL(10,2),
                        PaymentType VARCHAR(50),

                        CONSTRAINT fk_sale_user
                            FOREIGN KEY (UserID)
                            REFERENCES User(UserID),

                        CONSTRAINT fk_sale_customer
                            FOREIGN KEY (CustomerID)
                            REFERENCES Customer(CustomerID)
                    ) ENGINE=InnoDB;  
                """
            );

            // Table SaleItem
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS SaleItem (
                        SaleItemID INT AUTO_INCREMENT PRIMARY KEY,
                        SaleID INT NOT NULL,
                        ProductID INT NOT NULL,
                        Quantity INT NOT NULL,
                        Price DECIMAL(10,2) NOT NULL,
                        Discount DECIMAL(10,2),

                        CONSTRAINT fk_saleitem_sale
                            FOREIGN KEY (SaleID)
                            REFERENCES Sale(SaleID),

                        CONSTRAINT fk_saleitem_product
                            FOREIGN KEY (ProductID)
                            REFERENCES Product(ProductID)
                    ) ENGINE=InnoDB;
                """
            );

            // Table Receipt
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS Receipt (
                        ReceiptID INT AUTO_INCREMENT PRIMARY KEY,
                        SaleID INT NOT NULL,
                        StoreName VARCHAR(100),
                        Item TEXT,
                        Totals DECIMAL(10,2),
                        DateTime DATETIME NOT NULL,

                        CONSTRAINT fk_receipt_sale
                            FOREIGN KEY (SaleID)
                            REFERENCES Sale(SaleID)
                    ) ENGINE=InnoDB;        
                """
            );

            // Table InventoryAdjustment
            statement.execute(
                """
                    CREATE TABLE IF NOT EXISTS InventoryAdjustment (
                        AdjustmentID INT AUTO_INCREMENT PRIMARY KEY,
                        ProductID INT NOT NULL,
                        AdjustmentDate DATETIME NOT NULL,
                        QuantityChange INT NOT NULL,
                        Reason VARCHAR(255),
                        UserID INT NOT NULL,

                        CONSTRAINT fk_inventory_product
                            FOREIGN KEY (ProductID)
                            REFERENCES Product(ProductID),

                        CONSTRAINT fk_inventory_user
                            FOREIGN KEY (UserID)
                            REFERENCES User(UserID)
                    ) ENGINE=InnoDB; 
                """
            );

            System.out.println(Color.GREEN + "🌽 Database migrated successfully." + Color.RESET);
        } catch (SQLException e) {System.out.println(Color.RED + "❌ Error migrating database: " + e.getMessage() + Color.RESET);}
    }
}
