package com.example.authserver.controller;

import org.springframework.web.bind.annotation.PostMapping;

public class AuthController {
    @PostMapping("/signin")
    public String signIn() {
        return "Login!";
    }

    @PostMapping("/signup")
    public String signUp() {
        return "signup!";
    }
}
