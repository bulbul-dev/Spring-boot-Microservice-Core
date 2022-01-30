package com.microservice.gatewayservice.service;

import com.microservice.gatewayservice.VO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SystemResAuthCheckService {

    @Autowired
    private RestTemplate restTemplate;

    public static boolean checkAuth(String token, ServerHttpRequest request) {
        System.out.println("token: " + token + " uri: " + request.getURI());
        return true;
    }



}
