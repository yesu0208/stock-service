package arile.toy.stock_service.domain.naverstock;

public record StockData(
        String cd,
        String nm,
        Integer sv,
        Integer nv,
        Integer cv,
        Double cr,
        String rf,
        String mt,
        String ms,
        String tyn,
        Integer pcv,
        Integer ov,
        Integer hv,
        Integer lv,
        Integer ul,
        Integer ll,
        Long aq,
        Long aa,
        Double nav,
        Integer keps,
        Integer eps,
        Double bps,
        Integer cnsEps,
        Double dv,
        NxtOverMarketPriceInfo nxtOverMarketPriceInfo // nxt 거래 허용 안되면 null
) {
}
