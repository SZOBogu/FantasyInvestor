package helpers;

import entities.AssetEntity;
import entities.PortfolioEntity;

public class AvailableCreditGetter {
    public static int getAvailableCredit(PortfolioEntity portfolio){
        for(AssetEntity asset : portfolio.getAssets()){
            if(asset.getStock().getName().equals("Cash")){
                return asset.getQuantity();
            }
        }
        return 0;
    }
}
