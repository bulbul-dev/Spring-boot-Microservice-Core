package com.microservice.userservice.service;

import com.microservice.userservice.VO.Department;
import com.microservice.userservice.VO.ResponseTemplateVO;
import com.microservice.userservice.entity.TestUser;
import com.microservice.userservice.repository.TestUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final TestUserRepository testUserRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    HttpServletRequest request;


    public TestUser createUser(TestUser user) {
        log.info("Creating user: {}", user);
        return testUserRepository.save(user);
    }

    public ResponseTemplateVO getUserWithDepartment(Long userId) {
        log.info("Getting user with department: {}", userId);
        log.info("Requesting user with department from microservice: {}", request.getHeader("Authorization"));
        Claims claims = decodeJWT(request.getHeader("Authorization"));
        log.info("Claims: {}", claims.get("sub"));
        ResponseTemplateVO vo = new ResponseTemplateVO();
        TestUser user = testUserRepository.findByUserId(userId);


        log.info("User: {}", user);

        Department department =
                restTemplate.getForObject("http://department-service/departments/" + user.getDepartmentId(),
                        Department.class);
        log.info("Department: {}", department);


        vo.setUser(user);
        vo.setDepartment(department);
        return vo;
    }

    public Claims decodeJWT(String token) {
        String onlyToken= token.substring(7);
        //This line will throw an exception if it is not a signed JWS (as expected)
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
             onlyToken = token.substring(7, token.length());
        }else{
            try {
                throw new Exception("Invalid token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                .parseClaimsJws(onlyToken).getBody();
        return claims;
    }
}
