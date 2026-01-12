package com.ott.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ott.backend.dto.LoginRequest;
import com.ott.backend.dto.RegisterRequest;
import com.ott.backend.entity.User;
import com.ott.backend.repository.UserRepository;
import com.ott.backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    /* ================= REGISTER ================= */
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email already registered");
                });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // plain for now

        return userRepository.save(user);
    }

    /* ================= LOGIN (JWT) ================= */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 🔐 GENERATE JWT
        String token = JwtUtil.generateToken(user.getEmail());

        // 🔁 RESPONSE FORMAT
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", token);

        return response;
    }
}
