package services;

import entities.AssetEntity;
import entities.PortfolioEntity;
import org.springframework.stereotype.Service;

@Service
public class AssetOperationService {
    public AssetEntity getAssetByName(PortfolioEntity portfolio, String name){
        for(AssetEntity asset : portfolio.getAssets()){
            if(asset.getStock().getName().equals(name)){
                return asset;
            }
        }
        return null;
    }

    public AssetEntity getAssetById(PortfolioEntity portfolio, int id){
        for(AssetEntity asset : portfolio.getAssets()){
            if(asset.getStock().getId() == id){
                return asset;
            }
        }
        return null;
    }
}
