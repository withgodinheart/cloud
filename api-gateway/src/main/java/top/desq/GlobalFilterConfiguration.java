package top.desq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GlobalFilterConfiguration {

    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return ((exchange, chain) -> {
            log.info("*** Second global prefilter is executed");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("*** Second global postfilter is executed");
            }));
        });
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return ((exchange, chain) -> {
            log.info("*** Third global prefilter is executed");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("*** First global postfilter is executed");
            }));
        });
    }
}
