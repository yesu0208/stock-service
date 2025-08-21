package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.InterestGroup;
import arile.toy.stock_service.domain.InterestStock;
import arile.toy.stock_service.domain.StockInfo;

import java.time.LocalDateTime;

public record InterestStockDto(
        Long id,
        InterestGroup interestGroup,
        StockInfo stockInfo,
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
        Integer fieldOrder,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAT,
        String modifiedBy
) {
    public static InterestStockDto fromEntity(InterestStock entity) {
        return new InterestStockDto(
                entity.getId(),
                entity.getInterestGroup(),
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
}
