package com.titov.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Configuration
@Slf4j
public class ResponseFilter {
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                if (requestHeaders.get(TrackingFilter.TRACK_ID) != null) {
                    log.debug("TRACK ID " + requestHeaders.get(TrackingFilter.TRACK_ID).stream().findFirst().get());
                    exchange.getResponse().getHeaders().add(TrackingFilter.TRACK_ID, UUID.randomUUID().toString());
                } else {
                    log.debug("NO TRACK ID");
                    exchange.getResponse().getHeaders().add(TrackingFilter.TRACK_ID, UUID.randomUUID().toString());
                }
            }));
        };
    }
}
