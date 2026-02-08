package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.mapper.dto.UserUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity @Table(name = "users")
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

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToOne(fetch = FetchType.LAZY) // (orphanRemoval = false) // ON DELETE SET NULL
    @JoinColumn(name = "profile_id", nullable = true)
    private BinaryContent profile;

//    @Setter
//    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE) // ON DELETE CASCADE
//    private UserStatus status;

    public void updateRole(Role role) {
        this.role = role;
    }


//    @Setter(AccessLevel.PROTECTED)
//    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL) // ON DELETE CASCADE
//    private UserStatus userStatus;
//
//    public void initUserStatus() {
//        if (this.userStatus == null) {
//            this.userStatus = new UserStatus(this, java.time.Instant.now());
//            log.info("✅✅✅status = [" + this.userStatus.toString() + "]");
//        }
//    }

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;

        if (this.profile != profile) {
            this.profile = null;
        }
        this.profile = profile;
    }

    public void updateFrom(UserUpdateRequest dtoUserUpdate, BinaryContent profile) {
        if (this.profile != profile) {
            this.profile = null;
        }
        this.profile = profile;

        this.username = dtoUserUpdate.newUsername();
        this.email = dtoUserUpdate.newEmail();

        if (null != dtoUserUpdate.newPassword()) {
            this.password = dtoUserUpdate.newPassword();
        }
    }
}