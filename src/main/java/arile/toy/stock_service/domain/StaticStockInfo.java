package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.constant.MarketClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(name = "static_stock_information")
@Entity
public class StaticStockInfo {

    @Id
    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "short_code", nullable = false)
    private String shortCode;

    @Column(name = "market_class", nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketClass marketClass;


    protected StaticStockInfo() {}


    public StaticStockInfo(String stockName, String shortCode, MarketClass marketClass) {
        this.stockName = stockName;
        this.shortCode = shortCode;
        this.marketClass = marketClass;
    }


    public StaticStockInfo(String stockName) {
        this.stockName = stockName;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof StaticStockInfo that)) return false;

        return Objects.equals(this.getStockName(), that.getStockName()) &&
                    Objects.equals(this.getShortCode(), that.getShortCode()) &&
                    Objects.equals(this.getMarketClass(), that.getMarketClass());
    }


    @Override
    public int hashCode() {

        return Objects.hash(getStockName(), getShortCode(), getMarketClass());

    }
}
