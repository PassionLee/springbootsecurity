package com.passion.practice.springbootsecurity.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author: lsl
 * @date: 2020-07-09
 */
public class CustomSecurityException extends AuthenticationException {

    public CustomSecurityException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomSecurityException(String msg) {
        super(msg);
    }
}
