package com.example.authserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class ResourceController {

    @GetMapping("/health")
    public String health() {
        return "healthy!";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin!";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "User!";
    }

    @GetMapping("/all")
    public String allRolesEndpoint() {
        return "All Roles!";
    }

    @DeleteMapping("/delete")
    public String deleteEndpoint(@RequestBody String s) {
        return "I am deleting " + s;
    }
}
