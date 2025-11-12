// src/main/java/com/manwhas_api/manwhas/dev/JwtDiagController.java
package com.manwhas_api.manwhas.controller;

import com.manwhas_api.manwhas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/jwt")
public class JwtDiagController {
    private final JwtService jwt;

    @GetMapping("/check")
    public Map<String,Object> check(@RequestParam String token) {
        boolean valid = jwt.isValid(token.trim());
        String sub = valid ? jwt.getSubject(token.trim()) : null;
        return Map.of("valid", valid, "subject", sub);
    }
}
