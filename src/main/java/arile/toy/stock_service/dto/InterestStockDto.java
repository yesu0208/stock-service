package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.InterestStock;
import arile.toy.stock_service.domain.StockInfo;

import java.time.LocalDateTime;

public record InterestStockDto(
        Long id,
//        InterestGroup interestGroup,
        StockInfo stockInfo,
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
        Integer fieldOrder,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    // Entity -> Dto
    public static InterestStockDto fromEntity(InterestStock entity) {
        return new InterestStockDto(
                entity.getId(),
//                entity.getInterestGroup(),
                entity.getStockInfo(),
                entity.getBuyingPrice(),
                entity.getNumOfStocks(),
                entity.getBreakEvenPrice(),
                entity.getFieldOrder(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    // static method (전체)
    public static InterestStockDto of(
            Long id,
            StockInfo stockInfo,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer breakEvenPrice,
            Integer fieldOrder,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestStockDto(id, stockInfo, buyingPrice, numOfStocks,breakEvenPrice,fieldOrder, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // static method (일부)
    public static InterestStockDto of(
            String stockName,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer breakEvenPrice,
            Integer fieldOrder
    ) {
        return new InterestStockDto(null, new StockInfo(stockName), buyingPrice, numOfStocks, breakEvenPrice, fieldOrder, null, null, null, null);
    }
}
