package com.ott.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ✅ Subscription Plan (FREE / PREMIUM)
    @Enumerated(EnumType.STRING)
    @Column
    private SubscriptionPlan plan;


    // ✅ Automatically set when row is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // -------- Getters --------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public SubscriptionPlan getPlan() {
        return plan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // -------- Setters --------

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlan(SubscriptionPlan plan) {
        this.plan = plan;
    }
    @PrePersist
    public void prePersist() {
        if (this.plan == null) {
            this.plan = SubscriptionPlan.FREE;
        }
    }

}
