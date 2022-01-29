package com.microservice.gatewayservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/userServiceFallBack")
    public String userServiceFallBackMethod(){
        return "User Service is down, please try again later";
    }

    @GetMapping("/departmentServiceFallBack")
    public String departmentServiceFallBackMethod(){
        return "Department Service is down, please try again later";
    }

    @GetMapping("/authServiceFallBack")
    public String authServiceFallBackMethod(){
        return "Auth Service is down, please try again later";
    }


}
