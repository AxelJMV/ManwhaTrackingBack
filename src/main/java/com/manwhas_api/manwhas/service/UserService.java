package com.manwhas_api.manwhas.service;


import com.manwhas_api.manwhas.entities.User;
import com.manwhas_api.manwhas.exception.DuplicateFieldException;
import com.manwhas_api.manwhas.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.nio.channels.IllegalChannelGroupException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Transactional
    public User register(String email, String username, String rawPassword) {
        if (repo.existsByEmail(email)) {
            throw new DuplicateFieldException("email", "Email already in use");
        }
        if (repo.existsByUsername(username)) {
            throw new DuplicateFieldException("username", "Username already in use");
        }
        var user = User.builder()
                .email(email)
                .username(username)
                .passwordHash(encoder.encode(rawPassword))
                .build();
        try {
            return repo.save(user);
        } catch (DataIntegrityViolationException e) {
            // fallback por si fue una carrera: resolvemos qué campo chocó
            if (repo.existsByEmail(email)) {
                throw new DuplicateFieldException("email", "Email already in use");
            }
            if (repo.existsByUsername(username)) {
                throw new DuplicateFieldException("username", "Username already in use");
            }
            throw e; // desconocido
        }
    }

}
