package com.example.authserver.controller;

import com.example.authserver.models.*;
import com.example.authserver.reposiotory.RoleRepository;
import com.example.authserver.reposiotory.UserRepository;
import com.example.authserver.utils.JWTUtils;
import com.example.authserver.utils.payload.AuthResponse;
import com.example.authserver.utils.payload.SignInRequest;
import com.example.authserver.utils.payload.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> claims = new HashMap<>();
        claims.put("username", authentication.getName());
        claims.put("authorities",
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));

        String jwt = jwtUtils.createJwtForClaims(authentication.getName(), claims);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        Optional<User> user = userRepository.findByUsernameOrEmail(signUpRequest.getUsername(), signUpRequest.getEmail());

        if (user.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        Set<Role> userRoles = getUserRoles(signUpRequest);

        User newUser = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                userRoles);

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Hello from auth server");
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
