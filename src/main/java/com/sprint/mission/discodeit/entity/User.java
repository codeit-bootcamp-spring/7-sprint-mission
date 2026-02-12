package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.security.Role;
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

    @Enumerated(EnumType.STRING)
    @Column(name= "role",length = 20,nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;


    public static User createUserWithProfileFactory(String userName, String email, String password, BinaryContent profile){

        return new User(userName,email,password,Role.USER,profile);
    }

    public static User createUserFactory(String userName, String email, String password){

        return new User(userName,email,password,Role.USER,null);
    }
    public static User createAdminFactory(String userName, String email, String password){

        return new User(userName,email,password,Role.ADMIN,null);
    }

    public void updateUserRole(Role role){
        this.role = role;
    }
}
