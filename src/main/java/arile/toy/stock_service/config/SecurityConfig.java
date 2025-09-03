package arile.toy.stock_service.config;

import arile.toy.stock_service.service.GithubOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final GithubOAuth2UserService githubOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers((PathRequest.toStaticResources().atCommonLocations())).permitAll() // 정적 리소스는 검사의 대상 아님(view를 그려주는 여러 요소 : js, 마크업 file, ..)
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "interest-group",
                                "stocks/**"
                        ).permitAll() // 해당 정보는 허용
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(githubOAuth2UserService))
                )// OAuth2 로그인 활성화
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
//        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//
//        // OAuth2UserService : @FunctionalInterface - 람다식 지원
//        return userRequest -> GithubUser.from(delegate.loadUser(userRequest).getAttributes());
//    }
}
