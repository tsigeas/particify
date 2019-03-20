package de.thm.arsnova.service.comment;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
                .setApplicationDestinationPrefixes("/backend")
                .enableStompBrokerRelay("/topic")
                .setUserDestinationBroadcast("/queue/log-unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("arsnova")
                .setClientPasscode("arsnova");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

}