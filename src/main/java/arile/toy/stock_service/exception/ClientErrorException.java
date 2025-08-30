package arile.toy.stock_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientErrorException extends RuntimeException {

    private final HttpStatus status; // 어떤 client error 인지를 나타내기 위한 error status code

    // 생성자
    public ClientErrorException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
