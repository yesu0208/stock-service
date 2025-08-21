package repository;

import arile.toy.stock_service.domain.InterestStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestStockRepository extends JpaRepository<InterestStock, Long> {
}
