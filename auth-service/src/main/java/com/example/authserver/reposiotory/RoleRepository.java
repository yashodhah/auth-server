package com.example.authserver.reposiotory;

import com.example.authserver.models.Role;
import com.example.authserver.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(RoleEnum roleEnum);
}
