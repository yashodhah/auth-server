package com.example.authserver.utils.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Set<String> roles;
}
