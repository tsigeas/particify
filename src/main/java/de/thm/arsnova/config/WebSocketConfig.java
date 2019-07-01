package de.thm.arsnova.config;

import de.thm.arsnova.websocket.handler.AuthChannelInterceptorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final String MESSAGING_PREFIX = "/backend";
	private final String[] DESTINATION_PREFIX = {"/exchange", "/topic", "/queue"};
	private final String USER_REGISTRY_BROADCAST = "/topic/log-user-registry";
	private final String USER_DESTINATION_BROADCAST = "/queue/log-unresolved-user";

	@Value("${messaging.relay.enabled}") private Boolean relayEnabled;
	@Value("${messaging.relay.host}") private String relayHost;
	@Value("${messaging.relay.port}") private int relayPort;
	@Value("${messaging.relay.user}") private String relayUser;
	@Value("${messaging.relay.password}") private String relayPassword;

	private final AuthChannelInterceptorAdapter authChannelInterceptorAdapter;
	@Value(value = "${security.cors.origins:}") private String[] corsOrigins;

	@Autowired
	public WebSocketConfig(AuthChannelInterceptorAdapter authChannelInterceptorAdapter) {
		this.authChannelInterceptorAdapter = authChannelInterceptorAdapter;
	}



	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes(MESSAGING_PREFIX);

		if (relayEnabled) {
			config
					.enableStompBrokerRelay(DESTINATION_PREFIX)
					.setUserRegistryBroadcast(USER_REGISTRY_BROADCAST)
					.setUserDestinationBroadcast(USER_DESTINATION_BROADCAST)
					.setRelayHost(relayHost)
					.setRelayPort(relayPort)
					.setClientLogin(relayUser)
					.setClientPasscode(relayPassword);
		} else {
			config.enableSimpleBroker(DESTINATION_PREFIX);
		}
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOrigins(corsOrigins).withSockJS();
	}


	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(authChannelInterceptorAdapter);
	}

}