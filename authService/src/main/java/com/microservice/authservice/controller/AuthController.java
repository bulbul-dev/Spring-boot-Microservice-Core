package com.microservice.authservice.controller;


import com.microservice.authservice.exception.ResourceNotFoundException;
import com.microservice.authservice.model.Role;
import com.microservice.authservice.model.User;
import com.microservice.authservice.payload.request.LoginRequest;
import com.microservice.authservice.payload.request.SignupRequest;
import com.microservice.authservice.payload.response.BaseResponse;
import com.microservice.authservice.payload.response.JwtResponse;
import com.microservice.authservice.repository.RoleRepository;
import com.microservice.authservice.repository.UserRepository;
import com.microservice.authservice.security.jwt.JwtUtils;
import com.microservice.authservice.security.services.UserDetailsImpl;
import com.microservice.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


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
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest) {

        // create new user's account
        User user = new User(signupRequest.getPhone(),
                signupRequest.getEmail(),
                signupRequest.getUsername(),
               this.bCryptPasswordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            System.out.println("ROLES IS "+roles);
            Role userRole = roleRepository.getRoleByAuthority("ROLE_USER");
            if (userRole==null){
                throw new ResourceNotFoundException("Role not found");
            }
            roles.add(userRole);
        }else{
            strRoles.forEach(role -> {
                if ("ROLE_SUPER_ADMIN".equals(role)) {
                    Role superAdminRole = roleRepository.getRoleByAuthority("ROLE_SUPER_ADMIN");
                    if (superAdminRole == null) {
                        try {
                            throw new ResourceNotFoundException("Role not found");
                        } catch (ResourceNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    roles.add(superAdminRole);
                } else if ("ROLE_ADMIN".equals(role)) {
                    Role adminRole = roleRepository.getRoleByAuthority("ROLE_ADMIN");
                    if (adminRole == null) {
                        try {
                            throw new ResourceNotFoundException("Role not found");
                        } catch (ResourceNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.getRoleByAuthority("ROLE_USER");
                    if (userRole == null) {
                        try {
                            throw new ResourceNotFoundException("Role not found");
                        } catch (ResourceNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    roles.add(userRole);
                }
            });
        }

        System.out.println("ROLES IS "+roles);

        user.setRoles(roles);
        return this.userService.createUser(user);
    }

    @GetMapping("/auth/currentUser")
    public ResponseEntity<String> getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(optionalUser.orElse(null).getUsername());
    }

}
