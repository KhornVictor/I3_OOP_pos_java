package main.com.pos.model;

public class Sale {
    private int saleId;
    private int userId;
    private DateTime saleDate;
    private int customerId;
    private double total;
    private double discount;
    private String paymentType;

    public Sale() {}

    public Sale(int saleId, int userId, DateTime saleDate, int customerId, double total, double discount, String paymentType){
        this.saleId = saleId;
        this.userId = userId;
        this.saleDate = saleDate;
        this.customerId = customerId;
        setTotal(total);
        setDiscount(discount);
        setPaymentType(paymentType);
    }

    public int getSaleId() { return saleId; }
    public int getUserId() { return userId; }
    public DateTime getSaleDate() { return saleDate; }
    public int getCustomerId() { return customerId; }
    public double getTotal() { return total; }
    public double getDiscount() { return discount; }
    public String getPaymentType() { return paymentType; }

    public double getFinalTotal() {
        return total - discount;
    }

    public void setSaleId(int saleId) { this.saleId = saleId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setSaleDate(DateTime saleDate) { this.saleDate = saleDate; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public final void setTotal(double total) { 
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        this.total = total; 
    }
    public final void setDiscount(double discount) { 
        if (discount < 0 || discount > total) {
            throw new IllegalArgumentException("Discount must be between 0 and total");
        }
        this.discount = discount; 
    }
    public final void setPaymentType(String paymentType) { 
        if (paymentType == null || paymentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment type cannot be null or empty");
        }
        this.paymentType = paymentType; 
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", userId=" + userId +
                ", customerId=" + customerId +
                ", total=" + total +
                ", discount=" + discount +
                ", finalTotal=" + getFinalTotal() +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}