package arile.toy.stock_service.dto.request.interest;

import arile.toy.stock_service.dto.InterestStockDto;

public record InterestStockRequest(
        String stockName, // StockInfo 대신 stockName을 받음
        Integer buyingPrice,
        Integer numOfStocks,
        Integer fieldOrder
) {
    // static method
    public static InterestStockRequest of(
            String stockName,
            Integer buyingPrice,
            Integer numOfStocks,
            Integer fieldOrder
    ) {
        return new InterestStockRequest(
                stockName,
                buyingPrice,
                numOfStocks,
                fieldOrder
        );
    }

    // request -> Dto
    public InterestStockDto toDto(Double fee) {
        var breakEvenPrice = this.buyingPrice() == null ? null : (int) Math.round(this.buyingPrice() * (1 + fee));
        var totalBuyingPrice = this.buyingPrice() == null || this.numOfStocks() == null ?
                null : this.buyingPrice() * this.numOfStocks();
        return InterestStockDto.of(
                this.stockName(),
                this.buyingPrice(),
                this.numOfStocks(),
                breakEvenPrice,
                totalBuyingPrice,
                this.fieldOrder()
        );
    }

}
