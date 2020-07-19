package de.thm.arsnova.service.httpgateway.config

import de.thm.arsnova.service.httpgateway.filter.AuthFilter
import de.thm.arsnova.service.httpgateway.filter.RoomIdFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(HttpGatewayProperties::class)
class GatewayConfig (
        private val httpGatewayProperties: HttpGatewayProperties
) {
    @Bean
    fun myRoutes(
            builder: RouteLocatorBuilder,
            authFilter: AuthFilter,
            roomIdFilter: RoomIdFilter
    ): RouteLocator? {
        return builder.routes()
                .route("core") { p ->
                    p
                            .path("/room/**", "/auth/**", "/content/**", "/user/**", "/configuration/**", "/answer/**")
                            .uri(httpGatewayProperties.routing?.endpoints?.core)
                }
                .route("comment-service") { p ->
                    p
                            .path("/{roomId}/comment/**")
                            .filters { f ->
                                f.filter(authFilter.apply(AuthFilter.Config()))
                                f.filter(roomIdFilter.apply(RoomIdFilter.Config()))
                            }
                            .uri(httpGatewayProperties.routing?.endpoints?.commentService)
                }
                .route("comment-service-todo") { p ->
                    p
                            .path("/bonustoken/**", "/vote/**", "/settings/**")
                            .uri(httpGatewayProperties.routing?.endpoints?.commentService)
                }
                .build()
    }
}
