package com.microservice.authservice.service;

import com.microservice.authservice.model.User;
import com.microservice.authservice.payload.response.BaseResponse;
import com.microservice.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public BaseResponse create(User user) {
        try {
            userRepository.save(user);
            return new BaseResponse(true, "Successfully registered");
        } catch (Exception e) {
            return new BaseResponse(false, "Something went wrong");
        }
    }
}
