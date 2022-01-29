package com.microservice.authservice.service;

import com.microservice.authservice.exception.AlreadyExistsException;
import com.microservice.authservice.model.User;
import com.microservice.authservice.payload.response.BaseResponse;
import com.microservice.authservice.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ResponseEntity<?> createUser(User user) {
        Optional <User> local = this.userRepository.findByUsername(user.getUsername());
        if(local.isPresent()){
            System.out.println("User is already exists");
            throw new AlreadyExistsException("User already exists");
        }else{
            String randomCode = RandomString.make(64);

            this.userRepository.save(user);

            return ResponseEntity.ok(new BaseResponse(true, "Successfully registered"));
        }

    }
}
