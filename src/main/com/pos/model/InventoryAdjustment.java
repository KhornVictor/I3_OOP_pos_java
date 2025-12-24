package main.com.pos.model;

public class InventoryAdjustment {
    private int adjustmentId;
    private int productId;
    private DateTime adjustmentDate;
    private int quantityChange;
    private String reason;
    private int userId;

    public InventoryAdjustment() {}

    public InventoryAdjustment(int adjustmentId, int productId, DateTime adjustmentDate, int quantityChange, String reason, int userId) {
        this.adjustmentId = adjustmentId;
        this.productId = productId;
        this.adjustmentDate = adjustmentDate;
        setQuantityChange(quantityChange);
        setReason(reason);
        this.userId = userId;
    }

    public int getAdjustmentId() { return adjustmentId; }
    public int getProductId() { return productId; }
    public DateTime getAdjustmentDate() { return adjustmentDate; }
    public int getQuantityChange() { return quantityChange; }
    public String getReason() { return reason; }
    public int getUserId() { return userId; }

    public void setAdjustmentId(int adjustmentId) { this.adjustmentId = adjustmentId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setAdjustmentDate(DateTime adjustmentDate) { this.adjustmentDate = adjustmentDate; }
    public final void setQuantityChange(int quantityChange) { 
        if (quantityChange == 0) {
            throw new IllegalArgumentException("Quantity change cannot be zero");
        }
        this.quantityChange = quantityChange; 
    }
    public final void setReason(String reason) { 
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be null or empty");
        }
        this.reason = reason; 
    }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "InventoryAdjustment{" +
                "adjustmentId=" + adjustmentId +
                ", productId=" + productId +
                ", quantityChange=" + quantityChange +
                ", reason='" + reason + '\'' +
                ", userId=" + userId +
                '}';
    }
}
