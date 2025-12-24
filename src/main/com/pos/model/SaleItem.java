package main.com.pos.model;

public class SaleItem {
    private int saleItemId;
    private int saleId;
    private int productId;
    private int quantity;
    private double price;
    private double discount;

    public SaleItem() {}

    public SaleItem(int saleItemId, int saleId, int productId, int quantity, double price, double discount) {
        this.saleItemId = saleItemId;
        this.saleId = saleId;
        this.productId = productId;
        setQuantity(quantity);
        setPrice(price);
        setDiscount(discount);
    }

    public int getSaleItemId() { return saleItemId; }
    public void setSaleItemId(int saleItemId) { this.saleItemId = saleItemId; }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public final void setQuantity(int quantity) { 
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity; 
    }

    public double getPrice() { return price; }
    public final void setPrice(double price) { 
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price; 
    }

    public double getDiscount() { return discount; }
    public final void setDiscount(double discount) { 
        if (discount < 0 || discount > (price * quantity)) {
            throw new IllegalArgumentException("Discount must be between 0 and subtotal");
        }
        this.discount = discount; 
    }

    public double getSubtotal() {
        return (price * quantity) - discount;
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "saleItemId=" + saleItemId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}