package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.InterestStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestStockRepository extends JpaRepository<InterestStock, Long> {
    List<InterestStock> findAllByInterestGroupUnchangeableId(String unchangeableId);
}
