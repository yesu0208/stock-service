package arile.toy.stock_service.domain.interest;

import arile.toy.stock_service.domain.auditing.AuditingFields;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "interest_stocks")
@Entity
public class InterestStock extends AuditingFields {

    @Id
    @Column(name = "interest_stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestStockId;

    @Setter
    @JoinColumn(name = "interest_group_id", nullable = false)
    @ManyToOne(optional = false)
    private InterestGroup interestGroup;

    @Setter
    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Setter
    @Column (name = "buying_price")
    private Integer buyingPrice;

    @Setter
    @Column (name = "num_of_stocks")
    private Integer numOfStocks;

    @Setter
    @Column (name = "break_even_price")
    private Integer breakEvenPrice;

    @Setter
    @Column (name = "total_buying_price")
    private Integer totalBuyingPrice;

    @Setter
    @Column(name = "field_order", nullable = false)
    private Integer fieldOrder;


    protected InterestStock() {}


    public InterestStock(String stockName, Integer buyingPrice, Integer numOfStocks, Integer breakEvenPrice, Integer totalBuyingPrice, Integer fieldOrder){
        this.stockName = stockName;
        this.buyingPrice = buyingPrice;
        this.numOfStocks = numOfStocks;
        this.breakEvenPrice = breakEvenPrice;
        this.totalBuyingPrice = totalBuyingPrice;
        this.fieldOrder = fieldOrder;
    }


    public static InterestStock of(String stockName, Integer buyingPrice, Integer numOfStocks, Integer breakEvenPrice, Integer totalBuyingPrice, Integer fieldOrder) {
        return new InterestStock(stockName, buyingPrice, numOfStocks, breakEvenPrice, totalBuyingPrice, fieldOrder);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof InterestStock that)) return false;

        if (getInterestStockId() == null) {
            return Objects.equals(this.getInterestGroup(), that.getInterestGroup()) &&
                    Objects.equals(this.getStockName(), that.getStockName()) &&
                    Objects.equals(this.getBuyingPrice(), that.getBuyingPrice()) &&
                    Objects.equals(this.getNumOfStocks(), that.getNumOfStocks()) &&
                    Objects.equals(this.getBreakEvenPrice(), that.getBreakEvenPrice()) &&
                    Objects.equals(this.getTotalBuyingPrice(), that.getTotalBuyingPrice()) &&
                    Objects.equals(this.getFieldOrder(), that.getFieldOrder());
        }
        return Objects.equals(this.getInterestStockId(), that.getInterestStockId());
    }


    @Override
    public int hashCode() {
        if (getInterestStockId() == null) {
            return Objects.hash(getInterestGroup(), getStockName(), getBuyingPrice(), getNumOfStocks(), getBreakEvenPrice(), getTotalBuyingPrice(), getFieldOrder());
        }
        return Objects.hash(getInterestStockId());
    }
}

