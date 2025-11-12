// src/main/java/com/manwhas_api/manwhas/controller/AuthController.java
package com.manwhas_api.manwhas.controller;

import com.manwhas_api.manwhas.security.JwtService;
import com.manwhas_api.manwhas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public record LoginRequest(String usernameOrEmail, String password) {}
    public record TokenResponse(String token) {}
    public record ErrorResponse(String message) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(req.usernameOrEmail(), req.password());
            var result = authManager.authenticate(auth);
            var token = jwt.generate(result.getName(), Map.of("scope","USER"));
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials"));
        }
    }
}
