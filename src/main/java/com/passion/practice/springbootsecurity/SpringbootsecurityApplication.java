package com.passion.practice.springbootsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringbootsecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootsecurityApplication.class, args);
    }

}
