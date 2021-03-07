package me.mircea.cc.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "debug", havingValue = "true")
public class DebugConfiguration {
    @Bean
    public WebFilter logRequestWebFilter() {
        return new RequestLoggingFilter();
    }

    // Spring boot actuator might have something for this
    public class RequestLoggingDecorator extends ServerHttpRequestDecorator {
        public RequestLoggingDecorator(ServerHttpRequest delegate) {
            super(delegate);
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders headers = super.getHeaders();
            log.debug("Request: headers = {}", headers);

            return headers;
        }
    }

    public class RequestLoggingFilter implements WebFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            ServerWebExchangeDecorator decorator = new ServerWebExchangeDecorator(exchange) {
                @Override
                public ServerHttpRequest getRequest() {
                    return new RequestLoggingDecorator(exchange.getRequest());
                }
            };

            return chain.filter(decorator);
        }
    }
}