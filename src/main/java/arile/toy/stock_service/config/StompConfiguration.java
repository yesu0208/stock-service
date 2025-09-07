package arile.toy.stock_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override // 웹소켓 클라이언트가 어떤 경로로 서버로 접근해야 하는지를 (엔드포인트) 지정
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chats");
    }

    @Override // 클라이언트에서 메시지를 발행하고, 클라이언트는 브로커로부터 전달되는 메시지를 받기 위해 구독 신청 경로 지정
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); // 메시지를 퍼블리시하는 경로 지정
        registry.enableSimpleBroker("/sub"); // 메시지 구독 신청
    }
}
