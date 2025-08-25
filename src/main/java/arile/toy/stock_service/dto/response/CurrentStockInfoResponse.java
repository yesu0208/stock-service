package arile.toy.stock_service.dto.response;

import arile.toy.stock_service.dto.CurrentStockInfoDto;
import arile.toy.stock_service.dto.InterestGroupDto;

import java.time.LocalDateTime;

public record CurrentStockInfoResponse(
        String shortCode,
        String stockName,

        String marketState,
        String marketType,
        Integer nowValue,
        Integer changeValue,
        Double changeRate,
        String riseOrFall,
        Integer highestValue,
        Integer lowestValue,
        Integer upperLimit,
        Integer lowerLimit,
        Integer openValue,
        Integer standardValue,
        Long transactionVolume,
        Long transactionValue,
        LocalDateTime time
) {
    // Dto -> response
    public static CurrentStockInfoResponse fromDto(CurrentStockInfoDto dto) {
        return new CurrentStockInfoResponse(
                dto.shortCode(),
                dto.stockName(),
                dto.marketState(),
                dto.marketType(),
                dto.nowValue(),
                dto.changeValue(),
                dto.changeRate(),
                dto.riseOrFall(),
                dto.highestValue(),
                dto.lowestValue(),
                dto.upperLimit(),
                dto.lowerLimit(),
                dto.openValue(),
                dto.standardValue(),
                dto.transactionVolume(),
                dto.transactionValue(),
                dto.time()
        );
    }
}
