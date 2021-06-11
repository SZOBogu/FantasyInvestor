package entities;

import javax.persistence.*;
import java.util.List;

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
    private int currentPrice;

    @Basic
    @Column(name = "priceAtTheStarOfTheDay")
    private int priceAtTheStarOfTheDay;

    @Basic
    @Column(name = "priceAtTheStarOfTheWeek")
    private int priceAtTheStarOfTheWeek;

    @Basic
    @Column(name = "priceAtTheStarOfTheMonth")
    private int priceAtTheStarOfTheMonth;

    @Basic
    @Column(name = "priceAtTheStarOfTheYear")
    private int priceAtTheStarOfTheYear;

    @OneToMany(mappedBy="cart", cascade = CascadeType.ALL)
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

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getPriceAtTheStarOfTheDay() {
        return priceAtTheStarOfTheDay;
    }

    public void setPriceAtTheStarOfTheDay(int priceAtTheStarOfTheDay) {
        this.priceAtTheStarOfTheDay = priceAtTheStarOfTheDay;
    }

    public int getPriceAtTheStarOfTheWeek() {
        return priceAtTheStarOfTheWeek;
    }

    public void setPriceAtTheStarOfTheWeek(int priceAtTheStarOfTheWeek) {
        this.priceAtTheStarOfTheWeek = priceAtTheStarOfTheWeek;
    }

    public int getPriceAtTheStarOfTheMonth() {
        return priceAtTheStarOfTheMonth;
    }

    public void setPriceAtTheStarOfTheMonth(int priceAtTheStarOfTheMonth) {
        this.priceAtTheStarOfTheMonth = priceAtTheStarOfTheMonth;
    }

    public int getPriceAtTheStarOfTheYear() {
        return priceAtTheStarOfTheYear;
    }

    public void setPriceAtTheStarOfTheYear(int priceAtTheStarOfTheYear) {
        this.priceAtTheStarOfTheYear = priceAtTheStarOfTheYear;
    }

    public List<AssetEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetEntity> assets) {
        this.assets = assets;
    }
}
