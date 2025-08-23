package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.InterestGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestGroupRepository extends JpaRepository<InterestGroup, Long> {
    List<InterestGroup> findByUserId(String userId);
    Optional<InterestGroup> findByUserIdAndGroupName(String userId, String groupName);
    void deleteByUserIdAndGroupName(String userId, String groupName);
}
