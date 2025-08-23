package arile.toy.stock_service.dto.request;

import arile.toy.stock_service.dto.InterestStockDto;

public record InterestStockRequest(
//        StockInfo stockInfo,
        String stockName, // StockInfo 대신 stockName을 받음
        Integer buyingPrice,
        Integer numOfStocks,
//        Integer breakEvenPrice,
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
    public InterestStockDto toDto() {
        var breakEvenPrice = this.buyingPrice() == null ? null : (int) Math.round(this.buyingPrice() * 1.002);
        return InterestStockDto.of(
                this.stockName(),
                this.buyingPrice(),
                this.numOfStocks(),
                breakEvenPrice,
                this.fieldOrder()
        );
    }

    // Dto -> request는 필요 x (request는 주는 것은 아니므로)
}
