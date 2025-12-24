package main.com.pos.service;

import java.util.List;
import main.com.pos.dao.ProductDAO;
import main.com.pos.model.Product;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productDAO.getAll();
        if (products.isEmpty()) {
            System.out.println("⚠️ No products found");
        }
        return products;
    }

    public Product getProductById(int id) {
        if (id <= 0) {
            System.out.println("❌ Invalid product ID");
            return null;
        }
        Product product = productDAO.getById(id);
        if (product == null) {
            System.out.println("⚠️ Product not found with ID: " + id);
        }
        return product;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        if (categoryId <= 0) {
            System.out.println("❌ Invalid category ID");
            return List.of();
        }
        return productDAO.getByCategory(categoryId);
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            System.out.println("❌ Product cannot be null");
            return false;
        }
        try {
            if (product.getName() == null || product.getName().isEmpty()) {
                System.out.println("❌ Product name cannot be empty");
                return false;
            }
            if (product.getPrice() < 0) {
                System.out.println("❌ Product price cannot be negative");
                return false;
            }
            return productDAO.create(product);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Validation error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        if (product == null) {
            System.out.println("❌ Product cannot be null");
            return false;
        }
        if (product.getProductId() <= 0) {
            System.out.println("❌ Invalid product ID");
            return false;
        }
        try {
            return productDAO.update(product);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Validation error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        if (id <= 0) {
            System.out.println("❌ Invalid product ID");
            return false;
        }
        return productDAO.delete(id);
    }

    public boolean checkStockAvailability(int productId, int quantity) {
        if (productId <= 0 || quantity <= 0) {
            return false;
        }
        Product product = productDAO.getById(productId);
        if (product == null) {
            return false;
        }
        return product.getStockQuantity() >= quantity;
    }

    public boolean updateStock(int productId, int quantityChange) {
        if (productId <= 0) {
            System.out.println("❌ Invalid product ID");
            return false;
        }
        return productDAO.updateStock(productId, quantityChange);
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> allProducts = getAllProducts();
        return allProducts.stream()
                .filter(p -> p.getStockQuantity() <= threshold)
                .toList();
    }
}