package org.pinguweb.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody Map<String, String> credentials) {
        String dni = credentials.get("dni");
        String password = credentials.get("password");

        boolean authenticated = authenticate(dni, password);

        return ResponseEntity.ok(authenticated);
    }

    private boolean authenticate(String dni, String password) {
        return "admin".equals(dni) && "admin".equals(password);
    }
}