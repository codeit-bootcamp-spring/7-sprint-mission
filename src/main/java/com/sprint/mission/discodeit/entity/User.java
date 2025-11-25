package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User extends BaseUpdateEntity {

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String username;
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    // private UUID profileId;
    //널러블이 디폴트여도  하나있고 없어도된다는 표현을 하고싶었다

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_users_profile"), unique = true, nullable = true)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile) {
        //
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void update(String newUsername, String newEmail, String newPassword, BinaryContent newProfile) {

        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;

        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;

        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;

        }
        if (newProfile != null) {
            this.profile = newProfile;

        }


    }
}
