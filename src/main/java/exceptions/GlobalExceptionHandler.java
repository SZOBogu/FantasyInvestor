package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@EnableWebMvc
@ControllerAdvice(basePackages = {"controllers","entities", "services", "configs", "exceptions", "helpers", "pojos", "requests", "responses"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DatabaseConnectionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDetails> handleDatabaseException(Throwable e) {
        System.out.println("Global exception handler caught DatabaseConnectionException");
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDetails> handleInsufficientFundsException(Throwable e) {
        System.out.println("Global exception handler caught InsufficientFundsException");
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughStocksToSellException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDetails> handleNotEnoughStocksToSellException(Throwable e) {
        System.out.println("Global exception handler caught NotEnoughStocksToSellException");
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessToNonExistentResource.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDetails> handleAccessToNonExistentResource(Throwable e) {
        System.out.println("Global exception handler caught AccessToNonExistentResource");
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }
}
