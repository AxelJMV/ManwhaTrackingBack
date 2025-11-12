// src/main/java/com/manwhas_api/manwhas/service/AuthService.java
package com.manwhas_api.manwhas.service;

import com.manwhas_api.manwhas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public String loginAndIssueToken(String usernameOrEmail, String rawPassword) {
        Authentication auth = new UsernamePasswordAuthenticationToken(usernameOrEmail, rawPassword);
        Authentication result = authManager.authenticate(auth); // valida con UDS + BCrypt
        return jwt.generate(result.getName(), Map.of("scope", "USER"));
    }
}
