package arile.toy.stock_service.domain;

import arile.toy.stock_service.domain.constant.MarketClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Table(name = "stock_information")
@Entity
public class StockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private String shortCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketClass marketClass;

}
