package arile.toy.stock_service.exception.entity;

import arile.toy.stock_service.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class StaticStockInfoNotFoundException extends ClientErrorException {

    public StaticStockInfoNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Stock not found");
    }

    public StaticStockInfoNotFoundException(String stockName) {
        super(HttpStatus.NOT_FOUND, "Stock with stockName " + stockName + " not found");
    }

}

