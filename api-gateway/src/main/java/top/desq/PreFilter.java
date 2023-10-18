package top.desq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PreFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("*** First prefilter is executed");
        var requestPath = exchange.getRequest().getPath().toString();
        log.info("Request path: {}", requestPath);

        var headers = exchange.getResponse().getHeaders();
        var headerNames = headers.keySet();
        headerNames.forEach(headerName -> {
            var headerValue = headers.getFirst(headerName);
            log.info("HEADER {}: {}", headerName, headerValue);
        });

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
