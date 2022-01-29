package com.microservice.authservice.controller;


import com.microservice.authservice.model.User;
import com.microservice.authservice.payload.request.LoginRequest;
import com.microservice.authservice.payload.response.BaseResponse;
import com.microservice.authservice.payload.response.JwtResponse;
import com.microservice.authservice.repository.UserRepository;
import com.microservice.authservice.security.jwt.JwtUtils;
import com.microservice.authservice.security.services.UserDetailsImpl;
import com.microservice.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    UserRepository userRepository;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication);
            long timeout = jwtUtils.getJwtExpirationMs();

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> optionalUser = userRepository.findByEmail(userDetails.getEmail());

            return ResponseEntity.ok(new JwtResponse(true, optionalUser.orElse(null), token, "Bearer", timeout));

        } catch (AuthenticationException e) {
            return ResponseEntity.ok(new BaseResponse(false, "Email or password is incorrect"));
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody String userName) {
        // Persist user to some persistent storage
        System.out.println("Info saved...");

        return new ResponseEntity<String>("Registered", HttpStatus.OK);
    }

    @GetMapping("/auth/currentUser")
    public ResponseEntity<String> getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(optionalUser.orElse(null).getUsername());
    }

}
