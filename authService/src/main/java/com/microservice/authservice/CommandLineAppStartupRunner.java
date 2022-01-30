package com.microservice.authservice;

import com.microservice.authservice.model.Role;
import com.microservice.authservice.model.User;
import com.microservice.authservice.repository.RoleRepository;
import com.microservice.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void createRoles(){

        if (!roleRepository.findByAuthority("ROLE_SUPER_ADMIN").isPresent()) {
            roleRepository.save(new Role(1L, "ROLE_SUPER_ADMIN", "ROLE_SUPER_ADMIN"));
        }
        if (!roleRepository.findByAuthority("ROLE_ADMIN").isPresent()) {
            roleRepository.save(new Role(2L, "ROLE_ADMIN", "ROLE_ADMIN"));
        }
        if (!roleRepository.findByAuthority("ROLE_USER").isPresent()) {
            roleRepository.save(new com.microservice.authservice.model.Role(3L,"ROLE_USER", "ROLE_USER"));
        }
    }

    public void createUser(){

        Role roleSuperAdmin=this.roleRepository.getRoleByAuthority("ROLE_SUPER_ADMIN");
        Role roleAdmin=this.roleRepository.getRoleByAuthority("ROLE_ADMIN");
        Role roleUser=this.roleRepository.getRoleByAuthority("ROLE_USER");


        Optional<User> superAdmin = this.userRepository.findByUsername("super-admin");
        if (!superAdmin.isPresent()) {
            Set<Role> rolesSuperAdminSet = new HashSet<>();
            User user = new User();

            user.setUsername("super-admin");
            user.setPassword(bCryptPasswordEncoder.encode("super-admin"));
            user.setEmail("super-admin@sa.com");
            user.setPhone("+923335555555");
            rolesSuperAdminSet.add(roleSuperAdmin);
            rolesSuperAdminSet.add(roleAdmin);
            user.setRoles(rolesSuperAdminSet);

            this.userRepository.save(user);
        }


        Optional<User> admin = this.userRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            Set<Role> rolesAdminSet = new HashSet<>();
            User user = new User();
            user.setUsername("admin");
            user.setPassword(bCryptPasswordEncoder.encode("admin"));
            user.setEmail("admin@gmail.com");
            user.setPhone("+9233444555");
            rolesAdminSet.add(roleAdmin);
            user.setRoles(rolesAdminSet);
            this.userRepository.save(user);
        }


        Optional<User> user = this.userRepository.findByUsername("user");
        if (!user.isPresent()) {
            Set<Role> rolesUserSet = new HashSet<>();
            User user1 = new User();
            user1.setUsername("user");
            user1.setPassword(bCryptPasswordEncoder.encode("user"));
            user1.setEmail("user@user.com");
            user1.setPhone("+9233355544555");
            rolesUserSet.add(roleUser);
            user1.setRoles(rolesUserSet);

            this.userRepository.save(user1);

        }
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---->CommandLineAppStartupRunner----->");
        this.createRoles();
        this.createUser();
    }
}
