package com.microservice.gatewayservice.filter;

import com.microservice.gatewayservice.VO.User;
import com.microservice.gatewayservice.exception.JwtTokenMalformedException;
import com.microservice.gatewayservice.exception.JwtTokenMissingException;
import com.microservice.gatewayservice.exception.RequestUrlNotPermitException;
import com.microservice.gatewayservice.service.SystemResAuthCheckService;
import com.microservice.gatewayservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtils jwtUtil;
    String token;

    private RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = Arrays.asList("/register", "/login");
        System.out.println("request.getURI() = " + request.getURI());

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }
             this.token = request.getHeaders().getOrEmpty("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                this.token = token.substring(7);
            }else{
                try {
                    throw new JwtTokenMalformedException("JWT Token is malformed");
                } catch (JwtTokenMalformedException e) {
                    e.printStackTrace();
                }
            }

            try {
                jwtUtil.validateToken(token);

                // For checking the user's authority
                String username = jwtUtil.getUsernameFromToken(token);
                if(!request.getURI().getPath().contains("/auth/currentUser")){
                    boolean authRequest = SystemResAuthCheckService.checkAuth(username,token,request);
                    if(!authRequest){
                        throw new RequestUrlNotPermitException("Request URL not permit");
                    }
                }

            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                // e.printStackTrace();
                System.out.println("JwtAuthenticationFilter: " + e.getMessage());
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            } catch (RequestUrlNotPermitException e) {
                e.printStackTrace();
            }

            Claims claims = jwtUtil.getClaims(token);
            System.out.println("claims: " + claims);
            exchange.getRequest().mutate().header("username", String.valueOf(claims.get("username"))).build();
        }

        return chain.filter(exchange);
    }

}
