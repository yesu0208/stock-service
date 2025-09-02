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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staticStockInfoId;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private String shortCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketClass marketClass;



    protected StaticStockInfo() {} // JPA Entity는 기본생성자 반드시 필요(프록시 생성을 위해)

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

        if (getStaticStockInfoId() == null) {
            return Objects.equals(this.getStockName(), that.getStockName()) &&
                    Objects.equals(this.getShortCode(), that.getShortCode()) &&
                    Objects.equals(this.getMarketClass(), that.getMarketClass());
        }
        return Objects.equals(this.getStaticStockInfoId(), that.getStaticStockInfoId());
    }

    @Override
    public int hashCode() {
        if (getStaticStockInfoId() == null) {
            return Objects.hash(getStockName(), getShortCode(), getMarketClass());
        }
        return Objects.hash(getStaticStockInfoId());
    }
}
