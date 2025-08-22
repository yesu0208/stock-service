package arile.toy.stock_service.dto.request;

import arile.toy.stock_service.dto.InterestStockDto;

public record InterestStockRequest(
//        StockInfo stockInfo,
        String stockName, // StockInfo 대신 stockName을 받음
        Integer buyingPrice,
        Integer numOfStocks,
        Integer breakEvenPrice,
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
                (int) Math.round(buyingPrice*1.0001), // 수수료 0.01% 가정
                fieldOrder
        );
    }

    // request -> Dto
    public InterestStockDto toDto() {
        return InterestStockDto.of(
                this.stockName,
                this.buyingPrice,
                this.numOfStocks,
                this.breakEvenPrice,
                this.fieldOrder
        );
    }

    // Dto -> request는 필요 x (request는 주는 것은 아니므로)
}
