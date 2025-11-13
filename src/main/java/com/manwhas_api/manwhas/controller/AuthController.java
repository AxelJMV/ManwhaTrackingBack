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
    private final AuthService authService;

    public record LoginRequest(String usernameOrEmail, String password) {}
    public record TokenResponse(String token) {}
    public record RegisterRequest(
            @jakarta.validation.constraints.Email String email,
            @jakarta.validation.constraints.NotBlank String username,
            @jakarta.validation.constraints.Size(min = 8, max = 72) String password
    ){}
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
            var auth = new UsernamePasswordAuthenticationToken(req.usernameOrEmail(), req.password());
            var result = authManager.authenticate(auth);
            var token = jwt.generate(result.getName(), Map.of("scope","USER"));
            return ResponseEntity.ok(new TokenResponse(token));
    }
    
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @jakarta.validation.Valid  RegisterRequest req) {
            var token = authService.registerAndIssueToken(req.email(),req.username(),req.password);
            //return ResponseEntity.status(201).body(new TokenResponse(token));
            return  ResponseEntity.status(201).body("Register successful");
    }
}
