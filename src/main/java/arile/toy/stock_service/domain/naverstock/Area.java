package arile.toy.stock_service.domain.naverstock;

import java.util.List;

public record Area(
        String name,
        List<StockData> datas

) {
}
