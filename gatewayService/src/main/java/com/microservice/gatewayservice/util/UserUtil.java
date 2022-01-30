package com.microservice.gatewayservice.util;

import com.microservice.gatewayservice.VO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserUtil {
    @Autowired
    private RestTemplate restTemplate;

    public User getUser() {
        return restTemplate.getForObject("http://auth-service/auth/currentUser", User.class);
    }
}
