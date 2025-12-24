package main.com.pos.controller;

import java.util.List;
import main.com.pos.model.Product;
import main.com.pos.service.ProductService;

public class ProductController {
    private final ProductService productService;

    public ProductController() {
        this.productService = new ProductService();
    }

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public Product getProductById(int id) {
        return productService.getProductById(id);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    public boolean addProduct(Product product) {
        return productService.addProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productService.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        return productService.deleteProduct(id);
    }

    public boolean checkStockAvailability(int productId, int quantity) {
        return productService.checkStockAvailability(productId, quantity);
    }

    public boolean updateStock(int productId, int quantityChange) {
        return productService.updateStock(productId, quantityChange);
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productService.getLowStockProducts(threshold);
    }

    public int getTotalProductCount() {
        return getAllProducts().size();
    }

    public double getTotalInventoryValue() {
        List<Product> products = getAllProducts();
        double total = 0;
        for (Product product : products) {
            total += product.getPrice() * product.getStockQuantity();
        }
        return total;
    }
}
