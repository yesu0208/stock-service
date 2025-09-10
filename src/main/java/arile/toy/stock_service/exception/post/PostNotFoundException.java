package arile.toy.stock_service.exception.post;

import arile.toy.stock_service.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ClientErrorException {

    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Post not found");
    }

    public PostNotFoundException(Long postId) {
        super(HttpStatus.NOT_FOUND, "Post with postId " + postId + " not found");
    }

}

