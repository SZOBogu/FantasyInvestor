package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "portfolio", schema = "FantasyInvestor")
public class PortfolioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_portfolio")
    private int id;


    @OneToMany(targetEntity = AssetEntity.class, mappedBy = "assetEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<AssetEntity> assets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AssetEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetEntity> assets) {
        this.assets = assets;
    }
}
