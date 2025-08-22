package arile.toy.stock_service.config;

import arile.toy.stock_service.dto.security.GithubUser;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers((PathRequest.toStaticResources().atCommonLocations())).permitAll() // 정적 리소스는 검사의 대상 아님(view를 그려주는 여러 요소 : js, 마크업 file, ..)
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "table-schema"
                        ).permitAll() // 해당 정보는 허용
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .oauth2Login(withDefaults()) // OAuth2 로그인 활성화
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        // OAuth2UserService : @FunctionalInterface - 람다식 지원
        return userRequest -> GithubUser.from(delegate.loadUser(userRequest).getAttributes());
    }
}
