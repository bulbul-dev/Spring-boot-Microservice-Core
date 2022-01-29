package com.microservice.userservice.controller;

import com.microservice.userservice.VO.ResponseTemplateVO;
import com.microservice.userservice.entity.TestUser;
import com.microservice.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public static final String USER_SERVICE="userService";

   // create user
    @PostMapping("/")
    public TestUser createUser(@RequestBody TestUser user) {
        log.info("UserController.createUser()");
        return userService.createUser(user);
    }

    @GetMapping("/{userId}")
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getUserFallback")
    public ResponseTemplateVO getUserWithDepartment(@PathVariable Long userId){
        log.info("UserController.getUserWithDepartment()");
        return userService.getUserWithDepartment(userId);
    }

    public ResponseTemplateVO getUserFallback(Exception e){
        log.info("UserController.getUserFallback()");
        return new ResponseTemplateVO(null,null);
    }



}
