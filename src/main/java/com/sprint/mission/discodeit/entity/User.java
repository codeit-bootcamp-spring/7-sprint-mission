package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity@Table(name = "users")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class User extends BaseUpdatableEntity {
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToOne(fetch = FetchType.LAZY) // (orphanRemoval = false) // ON DELETE SET NULL
    @JoinColumn(name = "profile_id", nullable = true)
    private BinaryContent profile;

//    @Setter
//    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE) // ON DELETE CASCADE
//    private UserStatus status;

    @Setter
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL) // ON DELETE CASCADE
    private UserStatus status;

    public void initStatus() {
        if (this.status == null) {
            this.status = new UserStatus(this, java.time.Instant.now());
        }
    }

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

}