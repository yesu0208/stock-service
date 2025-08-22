package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.InterestGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestGroupRepository extends JpaRepository<InterestGroup, Long> {
}
