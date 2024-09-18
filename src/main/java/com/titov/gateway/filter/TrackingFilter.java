package com.titov.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class TrackingFilter implements GlobalFilter {
    public static final String TRACK_ID = "track-id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        val headers = exchange.getRequest().getHeaders();
        if (headers.get(TRACK_ID) == null) {
        val id= UUID.randomUUID().toString();
            log.debug("add track id" + id);
            exchange = exchange.mutate().request(exchange
                            .getRequest()
                            .mutate()
                            .header(TRACK_ID, id)
                            .build())
                    .build();
        }

        return chain.filter(exchange);
    }
}
