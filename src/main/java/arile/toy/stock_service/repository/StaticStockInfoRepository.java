package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.StaticStockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaticStockInfoRepository extends JpaRepository<StaticStockInfo, Long> {
    Optional<StaticStockInfo> findByStockName(String stockName);
}
