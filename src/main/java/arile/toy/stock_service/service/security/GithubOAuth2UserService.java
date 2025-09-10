package arile.toy.stock_service.service.security;

import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.dto.security.GithubUser;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final GithubUserInfoRepository githubUserInfoRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String unchangeableId = String.valueOf(attributes.get("id"));
        String id = (String) attributes.get("login");
        String name = (String) attributes.get("name"); // nullable
        String email = (String) attributes.get("email"); // nullable

        name = (name == null) ? id : name;

        // 로그인 시마다 update or insert
        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElse(new GithubUserInfo());

        githubUserInfo.setUnchangeableId(unchangeableId);
        githubUserInfo.setId(id);
        githubUserInfo.setName(name);
        githubUserInfo.setEmail(email);
        githubUserInfo.setLastLoginAt(java.time.LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        githubUserInfo.setFee(0.0); // 수수료+세금 입력 안하면 기본으로 0 (초기 생성할 떄)

        githubUserInfoRepository.save(githubUserInfo);

        return new GithubUser(
                githubUserInfo.getUnchangeableId(),
                githubUserInfo.getId(),
                githubUserInfo.getName(),
                githubUserInfo.getEmail()
        );
    }

}
