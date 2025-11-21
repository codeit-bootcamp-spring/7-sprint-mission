package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.util.StaticString;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseUpdatableEntity implements Serializable {

    @Column(name = "username",nullable = false, unique = true,length = 50)
    private String userName;

    @Column(name = "email",nullable = false, unique = true,length = 100)
    private String email;

    @Column(name = "password",length = 60,nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent binaryContent;

}
