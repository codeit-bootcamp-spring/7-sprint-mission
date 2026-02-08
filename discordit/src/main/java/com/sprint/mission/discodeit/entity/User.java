package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Setter
    private String username;

    @Column(nullable = false)
    @Setter
    private String password;

    @Email
    @Column(unique = true, nullable = false)
    @Setter
    private String email;

    @OneToOne
    @Setter
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    @Transient
    @Setter
    private Boolean online;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void updateRole(Roles role) {
        this.role = role;
    }
}
