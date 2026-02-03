package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "persistent_logins",
        indexes = @Index(name= "idx_persistence_logins_username",columnList = "username")
)
@NoArgsConstructor
@Getter
public class PersistenceLogin {

    @Id
    @Column(length = 64,nullable = false)
    private String series;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant lastUsed;

    public void updateToken(){
        this.token = generateToken();
        this.lastUsed = Instant.now();
    }

    private String generateToken(){
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    public PersistenceLogin(UUID userId){
        this.series = UUID.randomUUID().toString();
        this.userId = userId;
        this.token = generateToken();
        this.lastUsed = Instant.now();

    }
}
