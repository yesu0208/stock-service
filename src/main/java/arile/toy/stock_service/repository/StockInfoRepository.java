package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.InterestGroup;
import arile.toy.stock_service.domain.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
    Optional<StockInfo> findByStockName(String stockName);
}
