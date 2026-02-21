//package com.sprint.mission.discodeit.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import java.time.LocalDateTime;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//@Entity
//@Table(name = "refresh_tokens")
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
//@EntityListeners(AuditingEntityListener.class)
//public class RefreshToken {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
////    @Column(nullable = false, unique = true, length = 500)
////    private String token;
//
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
//
//    @Column(name = "expires_at", nullable = false)
//    private LocalDateTime expiresAt;
//
//    @CreatedDate
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//
//    public RefreshToken(Long userId, LocalDateTime expiresAt) {
//        this.userId = userId;
//        this.expiresAt = expiresAt;
//    }
//    /**
//     * 만료 여부 확인
//     */
//    public boolean isExpired() {
//        return LocalDateTime.now().isAfter(expiresAt);
//    }
//}
//
