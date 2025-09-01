package arile.toy.stock_service.dto;

import arile.toy.stock_service.domain.InterestGroup;
import arile.toy.stock_service.domain.InterestStock;

import java.time.LocalDateTime;
import java.util.Objects;

public record InterestStockDto(
        Long interestStockId,
//        InterestGroup interestGroup,
        String stockName,
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
        Integer totalBuyingPrice,
        Integer fieldOrder,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    // Entity -> Dto
    public static InterestStockDto fromEntity(InterestStock entity) {
        return new InterestStockDto(
                entity.getInterestStockId(),
//                entity.getInterestGroup(),
                entity.getStockName(),
                entity.getBuyingPrice(),
                entity.getNumOfStocks(),
                entity.getBreakEvenPrice(),
                entity.getTotalBuyingPrice(),
                entity.getFieldOrder(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    // static method (전체)
    public static InterestStockDto of(
            Long interestStockId,
            String stockName,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer breakEvenPrice,
            Integer totalBuyingPrice,
            Integer fieldOrder,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new InterestStockDto(interestStockId, stockName, buyingPrice, numOfStocks,breakEvenPrice,totalBuyingPrice, fieldOrder, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // static method (일부)
    public static InterestStockDto of(
            String stockName,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer breakEvenPrice,
            Integer totalBuyingPrice,
            Integer fieldOrder
    ) {
        return new InterestStockDto(null, stockName, buyingPrice, numOfStocks, breakEvenPrice, totalBuyingPrice, fieldOrder, null, null, null, null);
    }


    // Dto -> Entity
    public InterestStock createEntity() {
        return InterestStock.of( // id는 자동 생성 - 넣을 필요 x
                this.stockName,
                this.buyingPrice,
                this.numOfStocks,
                this.breakEvenPrice,
                this.totalBuyingPrice,
                this.fieldOrder);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof InterestStockDto that)) return false;

        if (interestStockId() == null) {
            return Objects.equals(this.stockName(), that.stockName()) &&
                    Objects.equals(this.buyingPrice(), that.buyingPrice()) &&
                    Objects.equals(this.numOfStocks(), that.numOfStocks()) &&
                    Objects.equals(this.breakEvenPrice(), that.breakEvenPrice()) &&
                    Objects.equals(this.totalBuyingPrice(), that.totalBuyingPrice()) &&
                    Objects.equals(this.fieldOrder(), that.fieldOrder()) &&
                    Objects.equals(this.createdAt(), that.createdAt()) &&
                    Objects.equals(this.createdBy(), that.createdBy()) &&
                    Objects.equals(this.modifiedAt(), that.modifiedBy()) &&
                    Objects.equals(this.modifiedBy(), that.modifiedBy());
        }
        return Objects.equals(this.interestStockId(), that.interestStockId());
    }

    @Override
    public int hashCode() {
        if (interestStockId() == null) {
            return Objects.hash(stockName(), buyingPrice(), numOfStocks(), breakEvenPrice(),
                    totalBuyingPrice(), fieldOrder(), createdAt(), createdBy(), modifiedAt(), modifiedBy());
        }
        return Objects.hash(interestStockId());
    }
}
