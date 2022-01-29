package com.microservice.userservice.repository;

import com.microservice.userservice.entity.TestUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser, Long> {
    TestUser findByUserId(Long userId);
}
