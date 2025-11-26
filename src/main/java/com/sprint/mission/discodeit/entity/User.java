package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUpdatableEntity implements Serializable {

    @Column(name = "username",nullable = false, unique = true,length = 50)
    private String userName;

    @Column(name = "email",nullable = false, unique = true,length = 100)
    private String email;

    @Column(name = "password",length = 60,nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private UserStatus userStatus;

    public static User createUserWithProfileFactory(String userName, String email, String password, BinaryContent profile){

        return new User(userName,email,password,profile,null);
    }

    public static User createUserFactory(String userName, String email, String password){

        return new User(userName,email,password,null,null);
    }



}
