package com.example.kltn.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "verificationtoken")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    private LocalDateTime expiryDate;

    public VerificationToken(String token, Account account) {
        this.token = token;
        this.account = account;
        this.expiryDate = LocalDateTime.now().plusHours(24);
    }

}
