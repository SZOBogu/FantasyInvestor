package entities;

import javax.persistence.*;

@Entity
@Table(name = "asset", schema = "FantasyInvestor")
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asset")
    private int id;

    @Basic
    @Column(name = "quantity")
    private int quantity;

    @Basic
    @Column(name = "buyprice")
    private double buyPrice;

    @ManyToOne(targetEntity = StockEntity.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="stock_id")
    private StockEntity stock;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public StockEntity getStock() {
        return stock;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "AssetEntity{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", buyPrice=" + buyPrice +
                ", stock Name=" + stock.getName() +
                ", stock current price =" + stock.getCurrentPrice() +
                '}';
    }
}
