package arile.toy.stock_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Table(name = "interest_stocks")
@Entity
public class InterestStock extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false) // 반드시 interestGroup을 가져야 함.
    private InterestGroup interestGroup;

    @Setter @Column(nullable = false) private String stockName;
    @Setter @Column private Integer buyingPrice;
    @Setter @Column private Integer numOfStocks;
    @Setter @Column private Integer breakEvenPrice;
    @Setter @Column(nullable = false) private Integer fieldOrder;


    protected InterestStock() {}

    public InterestStock(String stockName, Integer buyingPrice, Integer numOfStocks, Integer breakEvenPrice, Integer fieldOrder){
        this.stockName = stockName;
        this.buyingPrice = buyingPrice;
        this.numOfStocks = numOfStocks;
        this.breakEvenPrice = breakEvenPrice;
        this.fieldOrder = fieldOrder;
    }

    public static InterestStock of(String stockName, Integer buyingPrice, Integer numOfStocks, Integer breakEvenPrice, Integer fieldOrder) {
        return new InterestStock(stockName, buyingPrice, numOfStocks, breakEvenPrice, fieldOrder);
    }

}

