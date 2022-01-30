package com.microservice.gatewayservice.util;

import com.microservice.gatewayservice.VO.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private RestTemplate restTemplate;


}
