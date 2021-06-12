package responses;

import pojos.StockDataDigest;

import java.util.ArrayList;
import java.util.List;

public class StockListResponse {
    private List<StockDataDigest> stockData = new ArrayList();

    public void add(StockDataDigest stockDataDigest){
        this.stockData.add(stockDataDigest);
    }
}
