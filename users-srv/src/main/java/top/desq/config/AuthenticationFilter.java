package top.desq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.desq.dto.LoginRequest;
import top.desq.service.UserService;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Log4j2
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment environment; // updates via BUS

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    // will be triggered automatically by spring when user submit a login request
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("AuthenticationFilter !!!! <------------");

        try {
//            log.info("Data: {}", new String(request.getInputStream().readAllBytes()));
            log.info("getAuthenticationManager: {}", getAuthenticationManager());
            var credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var userResponse = userService.getUserByEmail(username);
        var expirationTime = Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration-time")));
        var tokenSecret = Objects.requireNonNull(environment.getProperty("token.secret"));
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        var secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        var now = Instant.now();

        var token = Jwts.builder()
                .setSubject(userResponse.getId().toString())
                .setExpiration(Date.from(now.plusMillis(expirationTime)))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userResponse.getId().toString());
    }
}
