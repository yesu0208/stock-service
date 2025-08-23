package arile.toy.stock_service.domain.naverstock;

import java.util.List;

public record Result(
        Integer pollingInterval,
        List<Area> areas,
        Long time
) {
}
