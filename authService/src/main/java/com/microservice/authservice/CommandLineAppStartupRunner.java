package com.microservice.authservice;

import com.microservice.authservice.model.User;
import com.microservice.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void createUser(){
        if (!userRepository.findByUsername("admin").isPresent()) {
            userRepository.save(new User(1, "admin", bCryptPasswordEncoder.encode("admin"), "admin@admin.com", "01753155400"));
        }
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("---->CommandLineAppStartupRunner----->");
        this.createUser();
    }
}
