package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    /* 🔥 추가: User ↔ BinaryContent (프로필 1:1 관계) */
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private BinaryContent profile;

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String name) {
        this.name = name;
    }

    /* 🔥 프로필 업데이트 */
    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }
}
