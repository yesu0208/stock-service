package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.StockInfo;
import arile.toy.stock_service.domain.constant.MarketClass;

public record StockInfoDto(
        Long id,
        String stockName,
        String shortCode,
        MarketClass marketClass
) {
    public static StockInfoDto fromEntity(StockInfo entity) {
        return new StockInfoDto(
                entity.getId(),
                entity.getStockName(),
                entity.getShortCode(),
                entity.getMarketClass());
    }
}
