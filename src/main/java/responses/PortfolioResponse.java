package responses;

import entities.AssetEntity;
import entities.PortfolioEntity;
import pojos.AssetDataDigest;

import java.util.ArrayList;
import java.util.List;

public class PortfolioResponse {
    private int id;
    private String username;
    private List<AssetDataDigest> assetData;

    public PortfolioResponse(int id, String username, PortfolioEntity portfolio) {
        this.id = id;
        this.username = username;
        this.assetData = new ArrayList<>();

        for(AssetEntity asset : portfolio.getAssets()){
            this.assetData.add(new AssetDataDigest(asset));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AssetDataDigest> getAssetData() {
        return assetData;
    }

    public void setAssetData(List<AssetDataDigest> assetData) {
        this.assetData = assetData;
    }
}
