package arile.toy.stock_service.exception.group;

import arile.toy.stock_service.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class GroupNotFoundException extends ClientErrorException {
    public GroupNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Group not found");
    }

    public GroupNotFoundException(String groupName) {
        super(HttpStatus.NOT_FOUND, "Group with groupName " + groupName + " not found");
    }

    public GroupNotFoundException(String unchangeableId, String groupName) {
        super(HttpStatus.NOT_FOUND, "Group with groupName " + groupName + " not found in user unchangeableId : " + unchangeableId);
    }
}

