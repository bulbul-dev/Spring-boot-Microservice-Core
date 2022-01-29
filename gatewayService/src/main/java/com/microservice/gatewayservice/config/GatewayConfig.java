package com.microservice.gatewayservice.config;

import com.microservice.gatewayservice.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()


                .route("product-service", r -> r.path("/product-service/**").filters(f -> f.filter(filter)).uri("lb://product-service"))
                .route("inventory-service", r -> r.path("/inventory-service/**").filters(f -> f.filter(filter)).uri("lb://inventory-service"))
                .route("order-service", r -> r.path("/order-service/**").filters(f ->  f.filter(filter)).uri("lb://order-service"))


                .route("auth-service", r -> r.path("/auth/**")
                        .filters( f->f.circuitBreaker(c -> c.setName("myCircuitBreaker")
                                        .setFallbackUri("forward:/authServiceFallBack")))
                        .uri("lb://auth-service"))


                .route("user-service", r -> r.path("/users/**")
                        .filters( f->f.circuitBreaker(c -> c.setName("myCircuitBreaker")
                                .setFallbackUri("forward:/userServiceFallBack"))
                                .filter((filter)))
                        .uri("lb://user-service"))


                .route("department-service", r -> r.path("/departments/**")
                        .filters( f->f.circuitBreaker(c -> c.setName("myCircuitBreaker")
                                .setFallbackUri("forward:/departmentServiceFallBack"))
                                .filter((filter)))
                        .uri("lb://department-service"))



                .build();

    }



}
