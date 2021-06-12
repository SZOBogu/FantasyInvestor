package requests;

public class SellStockRequest {
    private int stockId;
    private int quantity;
    private int sellingPrice;

    public SellStockRequest(int stockId, int quantity, int sellingPrice) {
        this.stockId = stockId;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
