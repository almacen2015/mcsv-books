package backend.gatewayserver.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${security.jwt.key.private}")
    private String secretKey;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String NOT_AUTHORIZED_MESSAGE = "Not Authorized";
        final String INVALID_TOKEN_MESSAGE = "Invalid token";
        final String HEADER_BEARER = "Bearer";
        final String HEADER_BEARER_WITH_SPACE = "Bearer ";
        final String HEADER_AUTHORITIES = "Authorities";
        final String HEADER_ROLES = "roles";
        final String HEADER_USER = "user";
        final String PATH_LOGIN =  "/login";

        ServerHttpRequest request = exchange.getRequest();

        if (request.getURI().getPath().contains(PATH_LOGIN)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(HEADER_BEARER)) {
            return unauthorized(exchange, NOT_AUTHORIZED_MESSAGE);
        }

        String token = authHeader.substring(7);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String roles = decodedJWT.getClaim(HEADER_AUTHORITIES).toString();

            ServerHttpRequest newRequest = exchange.getRequest()
                    .mutate()
                    .header(HttpHeaders.AUTHORIZATION, HEADER_BEARER_WITH_SPACE + token)
                    .header(HEADER_ROLES, roles)
                    .header(HEADER_USER, decodedJWT.getSubject())
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        } catch (Exception ex) {
            return unauthorized(exchange, INVALID_TOKEN_MESSAGE);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
