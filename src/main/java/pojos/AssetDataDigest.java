package pojos;

import entities.AssetEntity;
import entities.StockEntity;

public class AssetDataDigest {
    private int id;
    private String name;
    private double buyingPrice;
    private double currentPrice;

    public AssetDataDigest(AssetEntity assetEntity){
        this.id = assetEntity.getStock().getId();
        this.name = assetEntity.getStock().getName();
        this.buyingPrice = assetEntity.getBuyPrice();
        this.currentPrice = assetEntity.getStock().getCurrentPrice();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }
}
