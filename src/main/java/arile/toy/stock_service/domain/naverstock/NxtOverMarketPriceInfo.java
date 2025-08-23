package arile.toy.stock_service.domain.naverstock;

public record NxtOverMarketPriceInfo(
        String tradingSessionType,
        String overMarketStatus,
        String overPrice,
        CompareToPreviousPrice compareToPreviousPrice,
        String compareToPreviousClosePrice,
        String fluctuationsRatio,
        String localTradedAt,
        TradeStopType tradeStopType
) {
}
