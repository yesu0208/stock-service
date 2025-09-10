package arile.toy.stock_service.exception.chats;

import arile.toy.stock_service.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class ChatroomNotFoundException extends ClientErrorException {

    public ChatroomNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Chatroom not found");
    }

    public ChatroomNotFoundException(Long chatroomId) {
        super(HttpStatus.NOT_FOUND, "Chatroom with chatroomId " + chatroomId + " not found");
    }

}

