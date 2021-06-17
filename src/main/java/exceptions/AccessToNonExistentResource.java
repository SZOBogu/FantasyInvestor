package exceptions;

public class AccessToNonExistentResource extends RuntimeException{
    public AccessToNonExistentResource(){
        super("Trying to delete non existent id in database Exception");
    }
}
