package main.com.pos.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.com.pos.dao.OrderDAO;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Sale;
import main.com.pos.model.SaleItem;

public class OrderService {
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final ProductService productService;
    private String lastError = "";

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
        this.productService = new ProductService(productDAO);
    }

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.productService = new ProductService(productDAO);
    }

    public int createOrder(Sale sale) {
        if (sale == null) {
            System.out.println("❌ Sale cannot be null");
            return -1;
        }
        try {
            int saleId = orderDAO.createOrder(sale);
            if (saleId > 0) {
                System.out.println("✅ Order created with ID: " + saleId);
            }
            return saleId;
        } catch (Exception e) {
            lastError = "Error creating order: " + e.getMessage();
            System.out.println("❌ " + lastError);
            return -1;
        }
    }

    public boolean addItemToOrder(SaleItem item) {
        if (item == null) {
            System.out.println("❌ Item cannot be null");
            return false;
        }
        if (item.getSaleId() <= 0 || item.getProductId() <= 0) {
            System.out.println("❌ Invalid sale or product ID");
            return false;
        }
        try {
            // Check stock availability
            if (!productService.checkStockAvailability(item.getProductId(), item.getQuantity())) {
                lastError = "Insufficient stock for product: " + item.getProductId();
                System.out.println("❌ " + lastError);
                return false;
            }
            // Add item to order
            boolean added = orderDAO.addOrderItem(item);
            if (!added) {
                lastError = "Failed to add item to order: ProductID=" + item.getProductId();
                System.out.println("❌ " + lastError);
            }
            return added;
        } catch (IllegalArgumentException e) {
            lastError = "Validation error: " + e.getMessage();
            System.out.println("❌ " + lastError);
            return false;
        }
    }

    public String getLastError() { return lastError; }

    public Sale getOrder(int saleId) {
        if (saleId <= 0) {
            System.out.println("❌ Invalid sale ID");
            return null;
        }
        Sale sale = orderDAO.getOrderById(saleId);
        if (sale == null) {
            System.out.println("⚠️ Order not found with ID: " + saleId);
        }
        return sale;
    }

    public List<SaleItem> getOrderDetails(int saleId) {
        if (saleId <= 0) {
            System.out.println("❌ Invalid sale ID");
            return new ArrayList<>();
        }
        return orderDAO.getOrderItems(saleId);
    }

    public List<Sale> getAllOrders() {
        List<Sale> orders = orderDAO.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("⚠️ No orders found");
        }
        return orders;
    }

    public List<Sale> getUserOrders(int userId) {
        if (userId <= 0) {
            System.out.println("❌ Invalid user ID");
            return new ArrayList<>();
        }
        return orderDAO.getOrdersByUser(userId);
    }

    public List<Sale> getOrdersByDate(LocalDate date) {
        if (date == null) {
            System.out.println("❌ Date cannot be null");
            return new ArrayList<>();
        }
        return orderDAO.getOrdersByDate(date);
    }

    public List<Sale> getOrdersBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            System.out.println("❌ Date range cannot be null");
            return new ArrayList<>();
        }
        if (to.isBefore(from)) {
            System.out.println("❌ End date must be after start date");
            return new ArrayList<>();
        }
        return orderDAO.getOrdersBetween(from, to);
    }

    public double calculateOrderTotal(Sale sale) {
        if (sale == null) {
            return 0.0;
        }
        return sale.getFinalTotal();
    }

    public boolean completeOrder(Sale sale, List<SaleItem> items) {
        if (sale == null || items == null || items.isEmpty()) {
            System.out.println("❌ Invalid order or items");
            return false;
        }

        // Create the order
        int saleId = createOrder(sale);
        if (saleId <= 0) {
            return false;
        }

        // Add all items
        for (SaleItem item : items) {
            item.setSaleId(saleId);
            if (!addItemToOrder(item)) {
                System.out.println("❌ Failed to add item to order");
                return false;
            }
            // Update product stock
            productService.updateStock(item.getProductId(), -item.getQuantity());
        }

        System.out.println("✅ Order completed successfully");
        return true;
    }

    public double getOrderRevenue(String date) {
        double total = 0.0;
        List<Sale> orders = getAllOrders();
        for (Sale order : orders) {
            if (order.getSaleDate().getDate().toString().equals(date)) {
                total += order.getFinalTotal();
            }
        }
        return total;
    }
}
