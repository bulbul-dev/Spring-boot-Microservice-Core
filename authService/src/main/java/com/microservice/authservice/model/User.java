package com.microservice.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String gender;
    private String address;

    public User(long id, String username, String password, String email, String phone) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
