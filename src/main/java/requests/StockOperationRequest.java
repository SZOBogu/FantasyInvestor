package requests;

public class StockOperationRequest {
    private int stockId;
    private int quantity;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockOperationRequest{" +
                "stockId=" + stockId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
