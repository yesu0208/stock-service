package arile.toy.stock_service.dto.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public record GithubUser(
        String unchangeableId,
        String id,
        String name,
        String email
) implements OAuth2User {

//    public static GithubUser from(Map<String, Object> attributes) {
//        return new GithubUser( // 필요한 값만 추출
//                ((Number) attributes.get("id")).longValue(), // (github에서 변경 불가)
//                String.valueOf(attributes.get("login")), // (github에서 변경 가능)
//                String.valueOf(attributes.get("name")), // nullable
//                String.valueOf(attributes.get("email")) // nullable
//        );
//    }

    // 뽑을 정보는 위에 있으니, 따로 구현 x (나머지 정보는 버림)
    @Override public Map<String, Object> getAttributes() {return Map.of();}
    // 딱히 필요 x (권한 : admin, user, ...)
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {return List.of();}

    @Override // 닉네임이 세팅 안되면 name에 "null" 문자열이 내려옴.
    public String getName() {
        return name.equals("null") ? id : name; // name이 없다면 id, 있으면 name을 그대로 반환
    }
}
