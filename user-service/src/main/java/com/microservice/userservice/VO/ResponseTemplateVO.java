package com.microservice.userservice.VO;

import com.microservice.userservice.entity.TestUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVO {

    private TestUser user;
    private Department department;

}
