package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Entity@Table(name = "users")
public class User extends BaseUpdatableEntity {
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToOne(fetch = FetchType.LAZY) // (orphanRemoval = false) // ON DELETE SET NULL
    @JoinColumn(name = "profile_id") // nullable = true
    private BinaryContent profile;

    @Setter
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE) // ON DELETE CASCADE
    private UserStatus status;

}