package main.com.pos.controller;

import java.util.ArrayList;
import java.util.List;
import main.com.pos.model.Product;
import main.com.pos.model.Sale;
import main.com.pos.model.SaleItem;
import main.com.pos.service.OrderService;
import main.com.pos.service.ProductService;

public class POSController {
    private final OrderService orderService;
    private final ProductService productService;
    private Sale currentSale;
    private List<SaleItem> currentItems;

    public POSController() {
        this.orderService = new OrderService();
        this.productService = new ProductService();
        this.currentItems = new ArrayList<>();
    }

    public POSController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
        this.currentItems = new ArrayList<>();
    }

    public void initializeSale(Sale sale) {
        this.currentSale = sale;
        this.currentItems = new ArrayList<>();
        System.out.println("✅ New sale initialized");
    }

    public boolean addItemToCart(int productId, int quantity) {
        if (currentSale == null) {
            System.out.println("❌ No active sale. Initialize sale first.");
            return false;
        }

        if (!productService.checkStockAvailability(productId, quantity)) {
            System.out.println("❌ Insufficient stock");
            return false;
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            System.out.println("❌ Product not found");
            return false;
        }

        SaleItem item = new SaleItem(0, 0, productId, quantity, product.getPrice(), 0);
        currentItems.add(item);
        updateSaleTotal();
        System.out.println("✅ Item added to cart");
        return true;
    }

    public void removeItemFromCart(int index) {
        if (index >= 0 && index < currentItems.size()) {
            currentItems.remove(index);
            updateSaleTotal();
            System.out.println("✅ Item removed from cart");
        }
    }

    public void applyDiscount(double discountAmount) {
        if (currentSale != null) {
            try {
                currentSale.setDiscount(discountAmount);
                System.out.println("✅ Discount applied: " + discountAmount);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid discount: " + e.getMessage());
            }
        }
    }

    public boolean completeSale(int userId, int customerId, String paymentType) {
        if (currentSale == null || currentItems.isEmpty()) {
            System.out.println("❌ No items in cart");
            return false;
        }

        try {
            currentSale.setUserId(userId);
            currentSale.setCustomerId(customerId);
            currentSale.setPaymentType(paymentType);
            
            return orderService.completeOrder(currentSale, currentItems);
        } catch (Exception e) {
            System.out.println("❌ Error completing sale: " + e.getMessage());
            return false;
        }
    }

    public List<SaleItem> getCartItems() {
        return new ArrayList<>(currentItems);
    }

    public double getCartTotal() {
        if (currentSale != null) {
            return currentSale.getTotal();
        }
        return 0.0;
    }

    public double getCartFinalTotal() {
        if (currentSale != null) {
            return currentSale.getFinalTotal();
        }
        return 0.0;
    }

    public void clearCart() {
        currentItems.clear();
        currentSale = null;
        System.out.println("✅ Cart cleared");
    }

    private void updateSaleTotal() {
        if (currentSale != null) {
            double total = 0;
            for (SaleItem item : currentItems) {
                total += item.getSubtotal();
            }
            currentSale.setTotal(total);
        }
    }

    public List<Product> getAvailableProducts() {
        return productService.getAllProducts();
    }

    public Product getProduct(int productId) {
        return productService.getProductById(productId);
    }

    public Sale getSale(int saleId) {
        return orderService.getOrder(saleId);
    }

    public List<Sale> getSalesHistory(int userId) {
        return orderService.getUserOrders(userId);
    }
}
