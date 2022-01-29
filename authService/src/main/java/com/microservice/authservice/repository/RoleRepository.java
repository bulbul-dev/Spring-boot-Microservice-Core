package com.microservice.authservice.repository;

import com.microservice.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    Role getRoleByAuthority(String role_user);
    Optional<Role> findByAuthority(String authority);
}
