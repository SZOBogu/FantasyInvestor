package pojos;

import entities.StockEntity;

public class StockDataDigest {
    private int id;
    private String name;
    private double price;


    public StockDataDigest(StockEntity stock){
        this.id = stock.getId();
        this.name = stock.getName();
        this.price = stock.getCurrentPrice();
    }

}
