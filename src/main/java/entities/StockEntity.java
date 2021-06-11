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
    @Column(name = "currentPrice")
    private int buyPrice;
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

    @OneToMany(mappedBy="cart")
    private List<AssetEntity> assets;
}
