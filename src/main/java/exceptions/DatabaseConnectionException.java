package exceptions;

public class DatabaseConnectionException extends RuntimeException{
    public DatabaseConnectionException(){
        super("MySQL Session Exception");
    }
}
