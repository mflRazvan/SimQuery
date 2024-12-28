package com.example.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private Date expiryDate;

    public VerificationToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate(10);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return cal.getTime();
    }
}