package arile.toy.stock_service.repository;

import arile.toy.stock_service.domain.GithubUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubUserInfoRepository extends JpaRepository<GithubUserInfo, String> {
}
