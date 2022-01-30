package com.microservice.gatewayservice.exception;

import com.microservice.gatewayservice.payload.response.BaseResponse;

import javax.naming.AuthenticationException;

public class RequestUrlNotPermitException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public RequestUrlNotPermitException(String msg) {
        super(msg);
    }
}
