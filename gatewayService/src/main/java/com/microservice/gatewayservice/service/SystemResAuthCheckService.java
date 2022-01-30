package com.microservice.gatewayservice.service;


import com.microservice.gatewayservice.VO.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemResAuthCheckService {



    public static boolean checkAuth(String username,String token, ServerHttpRequest request) {

        log.info("username: {}", username);
        // fetch user from db
        RestTemplate restTemplate = new RestTemplate();
        // passing token  call rest template with passing  token header

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<User> userResponse = restTemplate.exchange("http://localhost:9002/auth/currentUser", HttpMethod.GET, entity, User.class);
        Objects.requireNonNull(userResponse.getBody());
        System.out.println(userResponse.getBody().getEmail());

        return true;
    }




}
