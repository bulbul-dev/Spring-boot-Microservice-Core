package com.microservice.gatewayservice.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String gender;
    private String address;
    private String groupUsername;

    // System Logs
    Date creationDateTime;
    String creationUser;
    Date lastUpdateDateTime;
    String lastUpdateUser;


}
