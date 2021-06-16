package pojos;

import entities.AssetEntity;
import entities.StockEntity;

public class AssetDataDigest {
    private int id;
    private String name;
    private int quantity;
    private double buyingPrice;
    private double currentPrice;

    public AssetDataDigest(AssetEntity assetEntity){
        this.id = assetEntity.getStock().getId();
        this.name = assetEntity.getStock().getName();
        this.quantity = assetEntity.getQuantity();
        this.buyingPrice = assetEntity.getBuyPrice();
        this.currentPrice = assetEntity.getStock().getCurrentPrice();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    @Override
    public String toString() {
        return "AssetDataDigest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", buyingPrice=" + buyingPrice +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
