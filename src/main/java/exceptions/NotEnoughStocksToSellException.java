package exceptions;

public class NotEnoughStocksToSellException extends RuntimeException{
    public NotEnoughStocksToSellException(){
        super("Not Enough Stocks To Sell");
    }
}
