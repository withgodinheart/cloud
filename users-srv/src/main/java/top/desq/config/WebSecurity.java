package top.desq.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.desq.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    @Value("${gateway.ip}") // not updates via BUS, stays as it was while startup
    private String gatewayIp;

    private final Environment environment; // updates via BUS
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        var hasIpAddressExpression = "hasIpAddress('%s')".formatted(environment.getProperty("gateway.ip"));
        // ! configure AuthenticationManagerBuilder
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        var authenticationManager = authenticationManagerBuilder.build();

        // ! create AuthenticationFilter
        var authenticationFilter = new AuthenticationFilter(authenticationManager, userService, environment);
//         authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url")); // TODO add if want custom login url

        http.csrf().disable();
        // http.csrf(AbstractHttpConfigurer::disable)

        http.authorizeHttpRequests()

                .requestMatchers(HttpMethod.POST, "/users")
                .access(new WebExpressionAuthorizationManager(hasIpAddressExpression))

                .requestMatchers(new AntPathRequestMatcher("/h2/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()

                .and()
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();
        // http.headers(header -> header.frameOptions().disable())

        // http.formLogin(Customizer.withDefaults())

        return http.build();
    }
}
