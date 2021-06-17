package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "portfolio", schema = "FantasyInvestor")
public class PortfolioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private int id;


    @OneToMany(mappedBy = "portfolioEntity", cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    //@JoinColumn(name="asset_id")
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

    @Override
    public String toString() {
        return "PortfolioEntity{" +
                "id=" + id +
                ", assets=" + assets +
                '}';
    }

    public AssetEntity getCash(){
        for(AssetEntity assetEntity : assets){
            if(assetEntity.getStock().getName().equals("Cash")){
                return assetEntity;
            }
        }
        return null;
    }
}
