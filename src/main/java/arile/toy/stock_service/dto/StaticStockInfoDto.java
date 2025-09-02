package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.StaticStockInfo;
import arile.toy.stock_service.domain.constant.MarketClass;

public record StaticStockInfoDto(
        Long staticStockInfoId,
        String stockName,
        String shortCode,
        MarketClass marketClass
) {
    public static StaticStockInfoDto fromEntity(StaticStockInfo entity) {
        return new StaticStockInfoDto(
                entity.getStaticStockInfoId(),
                entity.getStockName(),
                entity.getShortCode(),
                entity.getMarketClass());
    }
}
