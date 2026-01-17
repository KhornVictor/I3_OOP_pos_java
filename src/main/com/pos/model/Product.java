package main.com.pos.model;

public class Product {
    private int productId;
    private String name;
    private int categoryId;
    private double price;
    private int stockQuantity;
    private String image;

    public Product() {}

    public Product(int productId, String name, int categoryId, double price, int stockQuantity, String image) {
        this.productId = productId;
        setName(name);
        this.categoryId = categoryId;
        setPrice(price);
        setStockQuantity(stockQuantity);
        this.image = image;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getImage() { return image; }

    public void setProductId(int productId) { this.productId = productId; }
    public final void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        this.name = name; 
    }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public final void setPrice(double price) { 
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price; 
    }
    
    public final void setStockQuantity(int stockQuantity) { 
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = stockQuantity; 
    }

    public final void setImage(String image) { this.image = image; }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", image='" + image + '\'' +
                '}';
    }

    public Object getDescription() {
        return null;
    }
}
