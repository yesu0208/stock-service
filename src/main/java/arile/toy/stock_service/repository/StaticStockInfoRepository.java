package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.StaticStockInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface StaticStockInfoRepository extends JpaRepository<StaticStockInfo, Long> {

    @Override
    @NonNull
    @Cacheable(value = "stockNames")
    List<StaticStockInfo> findAll();
    Optional<StaticStockInfo> findByStockName(String stockName);

}
