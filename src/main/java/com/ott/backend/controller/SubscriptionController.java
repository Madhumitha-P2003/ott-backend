package com.ott.backend.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ott.backend.entity.Subscription;
import com.ott.backend.entity.User;
import com.ott.backend.repository.SubscriptionRepository;
import com.ott.backend.repository.UserRepository;
import com.ott.backend.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin(origins = "http://localhost:5173") // ✅ FIX CORS
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    /* ================= CHECK STATUS ================= */
    @GetMapping("/status")
    public boolean checkStatus(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String email = JwtUtil.extractEmail(authHeader.substring(7));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionRepository
                .findByUserIdAndStatus(user.getId(), "ACTIVE")
                .isPresent();
    }

    /* ================= SUBSCRIBE ================= */
    @PostMapping("/subscribe")
    public Subscription subscribe(
            @RequestParam String plan,
            HttpServletRequest request
    ) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String email = JwtUtil.extractEmail(authHeader.substring(7));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // prevent duplicate active subscription
        subscriptionRepository
                .findByUserIdAndStatus(user.getId(), "ACTIVE")
                .ifPresent(sub -> {
                    throw new RuntimeException("Already subscribed");
                });

        Subscription subscription = new Subscription();
        subscription.setUserId(user.getId());
        subscription.setPlan(plan);
        subscription.setStatus("ACTIVE");
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));

        return subscriptionRepository.save(subscription);
    }
}
