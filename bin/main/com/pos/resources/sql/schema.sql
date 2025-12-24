CREATE DATABASE IF NOT EXISTS i3_pos_db;
USE i3_pos_db;

// Table User

    CREATE TABLE IF NOT EXISTS User (
        UserID INT AUTO_INCREMENT PRIMARY KEY,
        Username VARCHAR(50) NOT NULL UNIQUE,
        Password VARCHAR(255) NOT NULL,
        Role VARCHAR(20) NOT NULL,
        Name VARCHAR(100),
        Email VARCHAR(100)
    ) ENGINE=InnoDB;

    INSERT INTO `User` (Username, Password, Role, Name, Email) VALUES
        ('admin1', 'pass123', 'ADMIN', 'John Admin', 'admin1@mail.com'),
        ('cashier1', 'cash123', 'CASHIER', 'Sara Cash', 'sara@mail.com'),
        ('cashier2', 'cash456', 'CASHIER', 'Mike Sale', 'mike@mail.com'),
        ('manager1', 'mgr123', 'MANAGER', 'Anna Manager', 'anna@mail.com'),
        ('staff1', 'staff123', 'STAFF', 'Tom Staff', 'tom@mail.com');


// Table Customer

    CREATE TABLE IF NOT EXISTS Customer (
        CustomerID INT AUTO_INCREMENT PRIMARY KEY,
        Name VARCHAR(100) NOT NULL,
        Email VARCHAR(100),
        Phone VARCHAR(20),
        Address TEXT
    ) ENGINE=InnoDB;

    INSERT INTO Customer (Name, Email, Phone, Address) VALUES
        ('Alice Brown', 'alice@mail.com', '012345678', 'Phnom Penh'),
        ('Bob Smith', 'bob@mail.com', '098765432', 'Siem Reap'),
        ('Charlie Lim', 'charlie@mail.com', '011223344', 'Battambang'),
        ('Dara Sok', 'dara@mail.com', '077889900', 'Kampong Cham'),
        ('Eva Long', 'eva@mail.com', '066554433', 'Takeo');


// Table Category

    CREATE TABLE IF NOT EXISTS Category (
        CategoryID INT AUTO_INCREMENT PRIMARY KEY,
        Name VARCHAR(100) NOT NULL
    ) ENGINE=InnoDB;

    INSERT INTO Category (Name) VALUES
        ('Beverages'),
        ('Snacks'),
        ('Dairy'),
        ('Electronics'),
        ('Stationery');


// Table Product

    CREATE TABLE IF NOT EXISTS Product (
        ProductID INT AUTO_INCREMENT PRIMARY KEY,
        Name VARCHAR(100) NOT NULL,
        CategoryID INT NOT NULL,
        Price DECIMAL(10,2) NOT NULL,
        Stock INT NOT NULL,

        CONSTRAINT fk_product_category
            FOREIGN KEY (CategoryID)
            REFERENCES Category(CategoryID)
    ) ENGINE=InnoDB;

    INSERT INTO Product (Name, CategoryID, Price, Stock) VALUES
        ('Coca Cola', 1, 1.00, 100),
        ('Potato Chips', 2, 1.50, 80),
        ('Milk 1L', 3, 2.20, 60),
        ('USB Cable', 4, 5.00, 40),
        ('Notebook', 5, 1.20, 150);


// Table Sale

    CREATE TABLE IF NOT EXISTS Sale (
        SaleID INT AUTO_INCREMENT PRIMARY KEY,
        DateTime DATETIME NOT NULL,
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

    INSERT INTO Sale (DateTime, UserID, CustomerID, Total, Discount, PaymentType) VALUES
        (NOW(), 2, 1, 5.00, 0.50, 'Cash'),
        (NOW(), 2, 2, 3.20, 0.00, 'Card'),
        (NOW(), 3, 3, 10.00, 1.00, 'Cash'),
        (NOW(), 2, 4, 7.50, 0.00, 'QR'),
        (NOW(), 3, 5, 12.00, 2.00, 'Card');

// Table SaleItem

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

    INSERT INTO SaleItem (SaleID, ProductID, Quantity, Price, Discount) VALUES
        (1, 1, 2, 1.00, 0.00),
        (2, 2, 1, 1.50, 0.00),
        (3, 4, 2, 5.00, 1.00),
        (4, 5, 3, 1.20, 0.00),
        (5, 3, 4, 2.20, 0.80);


// Table Receipt

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

    INSERT INTO Receipt (SaleID, StoreName, Item, Totals, DateTime) VALUES
        (1, 'I3 POS Store', 'Coca Cola x2', 4.50, NOW()),
        (2, 'I3 POS Store', 'Potato Chips x1', 1.50, NOW()),
        (3, 'I3 POS Store', 'USB Cable x2', 9.00, NOW()),
        (4, 'I3 POS Store', 'Notebook x3', 3.60, NOW()),
        (5, 'I3 POS Store', 'Milk 1L x4', 8.80, NOW());


// Table InventoryAdjustment

    CREATE TABLE IF NOT EXISTS InventoryAdjustment (
        AdjustmentID INT AUTO_INCREMENT PRIMARY KEY,
        ProductID INT NOT NULL,
        DateTime DATETIME NOT NULL,
        QuantityChanged INT NOT NULL,
        Reason VARCHAR(255),
        UserID INT NOT NULL,

        CONSTRAINT fk_inventory_product
            FOREIGN KEY (ProductID)
            REFERENCES Product(ProductID),

        CONSTRAINT fk_inventory_user
            FOREIGN KEY (UserID)
            REFERENCES User(UserID)
    ) ENGINE=InnoDB;

    INSERT INTO InventoryAdjustment (ProductID, DateTime, QuantityChanged, Reason, UserID) VALUES
        (1, NOW(), -10, 'Damaged items', 1),
        (2, NOW(), 20, 'New stock arrival', 4),
        (3, NOW(), -5, 'Expired products', 1),
        (4, NOW(), 15, 'Restock', 4),
        (5, NOW(), -8, 'Stock correction', 1);

