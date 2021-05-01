package de.thm.arsnova.service.httpgateway.filter

import de.thm.arsnova.service.httpgateway.model.RoomAccess
import de.thm.arsnova.service.httpgateway.model.RoomSubscription
import de.thm.arsnova.service.httpgateway.model.UserSubscription
import de.thm.arsnova.service.httpgateway.security.JwtTokenUtil
import de.thm.arsnova.service.httpgateway.service.RoomAccessService
import de.thm.arsnova.service.httpgateway.service.SubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

@Component
class AddMembershipFilter (
    private val jwtTokenUtil: JwtTokenUtil,
    private val roomAccessService: RoomAccessService,
    private val subscriptionService: SubscriptionService
) : AbstractGatewayFilterFactory<AddMembershipFilter.Config>(Config::class.java) {

    companion object {
        const val ARS_ROLE_HEADER = "ARS-Room-Role"
    }

    private val logger = LoggerFactory.getLogger(javaClass)
    private val tierIdToParticipantLimit = mutableMapOf<String, Int>()

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            chain.filter(exchange)
                .then(Mono.just(exchange))
                .filter { e ->
                    e.response.statusCode != null &&
                        e.response.statusCode!!.is2xxSuccessful &&
                        e.request.method != null
                }
                .filter { e ->
                    e.request.method == HttpMethod.POST
                }
                .flatMap { e ->
                    val roomId = e.response.headers.getFirst(RoomAuthFilter.ENTITY_ID_HEADER)!!
                    val revId = e.response.headers.getFirst(RoomAuthFilter.ENTITY_REVISION_HEADER)!!
                    /* Sending of the role as a header is a temporary solution for
                     * now to allow accessing it without parsing the body. */
                    val role = e.response.headers.getFirst(ARS_ROLE_HEADER)!!
                    val token = e.request.headers.getFirst(HttpHeaders.AUTHORIZATION)!!.removePrefix(RoomAuthFilter.BEARER_HEADER)
                    val userId = jwtTokenUtil.getUserIdFromPublicToken(token)
                    Mono.zip(
                        Mono.just(RoomAccess(
                            roomId,
                            userId,
                            revId,
                            role,
                            null
                        )),
                        subscriptionService
                            .getRoomSubscription(roomId, true)
                            .flatMap { roomSubscription: RoomSubscription ->
                                val maybeLimit = tierIdToParticipantLimit[roomSubscription.tierId]
                                if (maybeLimit != null) {
                                    Mono.just(maybeLimit)
                                } else {
                                    subscriptionService.getRoomParticipantLimit(roomSubscription.tierId)
                                }
                            }
                    )
                }
                .flatMap { tuple2: Tuple2<RoomAccess, Int> ->
                    roomAccessService
                        .postRoomAccessWithLimit(tuple2.t1, tuple2.t2)
                }
                .then()
        }
    }

    class Config {
        var name: String = "AddMembershipFilter"
    }
}
