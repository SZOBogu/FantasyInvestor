package helpers;

import entities.AssetEntity;
import entities.PortfolioEntity;

public class PortfolioValueCalculator {
    public static double getPortfolioValue(PortfolioEntity portfolio){
        double sum = 0.0;
        for(AssetEntity asset : portfolio.getAssets()){
            sum += asset.getStock().getCurrentPrice() * asset.getQuantity();
        }
        return sum;
    }
}
