package arile.toy.stock_service.exception;

import org.springframework.http.HttpStatus;

public class IllegalClientAccessException extends ClientErrorException {

    public IllegalClientAccessException() {
        super(HttpStatus.FORBIDDEN, "Illegal client access!");
    }

}

