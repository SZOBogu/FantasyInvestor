package entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stock", schema = "FantasyInvestor")
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private int id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "currentPrice")
    private double currentPrice;

    @Basic
    @Column(name = "priceAtTheStartOfTheDay")
    private double priceAtTheStartOfTheDay;

    @Basic
    @Column(name = "priceAtTheStartOfTheWeek")
    private double priceAtTheStartOfTheWeek;

    @Basic
    @Column(name = "priceAtTheStartOfTheMonth")
    private double priceAtTheStartOfTheMonth;

    @Basic
    @Column(name = "priceAtTheStartOfTheYear")
    private double priceAtTheStartOfTheYear;

    @OneToMany(mappedBy="assetEntity", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="stock_id")
    private List<AssetEntity> assets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getPriceAtTheStartOfTheDay() {
        return priceAtTheStartOfTheDay;
    }

    public void setPriceAtTheStartOfTheDay(double priceAtTheStartOfTheDay) {
        this.priceAtTheStartOfTheDay = priceAtTheStartOfTheDay;
    }

    public double getPriceAtTheStartOfTheWeek() {
        return priceAtTheStartOfTheWeek;
    }

    public void setPriceAtTheStartOfTheWeek(double priceAtTheStartOfTheWeek) {
        this.priceAtTheStartOfTheWeek = priceAtTheStartOfTheWeek;
    }

    public double getPriceAtTheStartOfTheMonth() {
        return priceAtTheStartOfTheMonth;
    }

    public void setPriceAtTheStartOfTheMonth(double priceAtTheStartOfTheMonth) {
        this.priceAtTheStartOfTheMonth = priceAtTheStartOfTheMonth;
    }

    public double getPriceAtTheStartOfTheYear() {
        return priceAtTheStartOfTheYear;
    }

    public void setPriceAtTheStartOfTheYear(double priceAtTheStartOfTheYear) {
        this.priceAtTheStartOfTheYear = priceAtTheStartOfTheYear;
    }

    public List<AssetEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetEntity> assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "StockEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEntity that = (StockEntity) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
