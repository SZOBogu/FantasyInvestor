package entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Table(name = "stock", schema = "FantasyInvestor")
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private int id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "currentPrice")
    private double currentPrice;

    @Basic
    @Column(name = "priceAtTheStarOfTheDay")
    private double priceAtTheStarOfTheDay;

    @Basic
    @Column(name = "priceAtTheStarOfTheWeek")
    private double priceAtTheStarOfTheWeek;

    @Basic
    @Column(name = "priceAtTheStarOfTheMonth")
    private double priceAtTheStarOfTheMonth;

    @Basic
    @Column(name = "priceAtTheStarOfTheYear")
    private double priceAtTheStarOfTheYear;

    @OneToMany(mappedBy="cart", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
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

    public double getPriceAtTheStarOfTheDay() {
        return priceAtTheStarOfTheDay;
    }

    public void setPriceAtTheStarOfTheDay(double priceAtTheStarOfTheDay) {
        this.priceAtTheStarOfTheDay = priceAtTheStarOfTheDay;
    }

    public double getPriceAtTheStarOfTheWeek() {
        return priceAtTheStarOfTheWeek;
    }

    public void setPriceAtTheStarOfTheWeek(double priceAtTheStarOfTheWeek) {
        this.priceAtTheStarOfTheWeek = priceAtTheStarOfTheWeek;
    }

    public double getPriceAtTheStarOfTheMonth() {
        return priceAtTheStarOfTheMonth;
    }

    public void setPriceAtTheStarOfTheMonth(double priceAtTheStarOfTheMonth) {
        this.priceAtTheStarOfTheMonth = priceAtTheStarOfTheMonth;
    }

    public double getPriceAtTheStarOfTheYear() {
        return priceAtTheStarOfTheYear;
    }

    public void setPriceAtTheStarOfTheYear(double priceAtTheStarOfTheYear) {
        this.priceAtTheStarOfTheYear = priceAtTheStarOfTheYear;
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
