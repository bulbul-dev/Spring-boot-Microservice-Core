package com.microservice.authservice.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservice.authservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private boolean status;
    private User user;
//    @JsonProperty("access_token")
    private String accessToken;
    private String tokenType;
    private long expiresAt;
}
