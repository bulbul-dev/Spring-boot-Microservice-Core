package com.microservice.userservice.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class Utils {

    @Value("${jwt.secret}")
    private static String SECRET;
    private static String TOKEN_PREFIX = "Bearer ";
    private static String HEADER_STRING = "Authorization";

    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
    }

}
