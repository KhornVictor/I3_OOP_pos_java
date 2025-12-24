package main.com.pos.model;

public class Receipt {
    private int receiptId;
    private int saleId;
    private String storeName;
    private String[] items;
    private double total;
    private DateTime saleDate;

    public Receipt() {}

    public Receipt(int receiptId, int saleId, String storeName, String[] items, double total, DateTime saleDate) {
        this.receiptId = receiptId;
        this.saleId = saleId;
        setStoreName(storeName);
        setItems(items);
        setTotal(total);
        this.saleDate = saleDate;
    }

    public int getReceiptId() { return receiptId; }
    public int getSaleId() { return saleId; }
    public String getStoreName() { return storeName; }
    public String[] getItems() { return items; }
    public double getTotal() { return total; }
    public DateTime getSaleDate() { return saleDate; }

    public void setReceiptId(int receiptId) { this.receiptId = receiptId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    public final void setStoreName(String storeName) { 
        if (storeName == null || storeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Store name cannot be null or empty");
        }
        this.storeName = storeName; 
    }
    public final void setItems(String[] items) { 
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Items cannot be null or empty");
        }
        this.items = items; 
    }
    public final void setTotal(double total) { 
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        this.total = total; 
    }
    public void setSaleDate(DateTime saleDate) { this.saleDate = saleDate; }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId=" + receiptId +
                ", saleId=" + saleId +
                ", storeName='" + storeName + '\'' +
                ", itemCount=" + (items != null ? items.length : 0) +
                ", total=" + total +
                '}';
    }
}