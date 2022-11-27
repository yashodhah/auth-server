package com.example.authserver.controller;

import com.example.authserver.models.*;
import com.example.authserver.reposiotory.RoleRepository;
import com.example.authserver.reposiotory.UserRepository;
import com.example.authserver.utils.JWTPayloadInfo;
import com.example.authserver.utils.JWTUtils;
import com.example.authserver.utils.payload.SignInRequest;
import com.example.authserver.utils.payload.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    AuthenticationManager authenticationManager;

    RoleRepository roleRepository;

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    JWTUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          RoleRepository roleRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JWTUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(new JWTPayloadInfo(authentication.getName()));

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        Set<Role> userRoles = getUserRoles(signUpRequest);
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                userRoles);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    private Set<Role> getUserRoles(SignUpRequest signUpRequest) {
        Set<Role> userRoles = new HashSet<>();
        Set<String> requestedRoles = signUpRequest.getRoles();

        if (requestedRoles == null || requestedRoles.isEmpty()) {
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            userRoles.add(userRole);
        } else {
            requestedRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                        userRoles.add(adminRole);

                        break;
                    case "MOD":
                        Role modRole = roleRepository.findByRole(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                        userRoles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                        userRoles.add(userRole);
                }
            });
        }


        return userRoles;
    }
}
