package exceptions;

public class DeletingNonExistentResource extends RuntimeException{
    public DeletingNonExistentResource(){
        super("Trying to delete non existent id in database Exception");
    }
}
