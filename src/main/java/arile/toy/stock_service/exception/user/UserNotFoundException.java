package arile.toy.stock_service.exception.user;

import arile.toy.stock_service.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClientErrorException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found");
    }

    public UserNotFoundException(String unchangeableId) {
        super(HttpStatus.NOT_FOUND, "User with unchangeableId " + unchangeableId + " not found");
    }
}

