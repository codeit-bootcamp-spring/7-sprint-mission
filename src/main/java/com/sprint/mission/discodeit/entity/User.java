package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.exception.ErrorType;
import com.sprint.mission.discodeit.entity.exception.ValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
//여러 개-> @AttributeOverrides({@AttributeOverride(~),...})
public class User extends BaseUpdatableEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

//    @Column(name = "last_active_at")
//    private Instant lastActiveAt;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    //REMOVE-> user삭제 시 binary_content 삭제 쿼리 날림
    //만약에 binary_contents가 user_id를 가지고 있었다면 on delete cascade로 처리 가능
    //orphanRemoval = true -> 프로필 교체 시 원래 참조하던 binary_content 삭제 쿼리 날림
    //이 기능은 jpa 기능이고 db에선 구현 불가(트리거는 요즘 안 쓰니까)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;


    public User(String email, String password, String username) {
        validateEmail(email);
        validatePassword(password);
        validateUsername(username);
        this.username = username;
        this.password = password;
        this.email = email;
        this.userStatus=new UserStatus(this);
    }

    public void updateActiveAt(Instant lastActiveAt){
        this.userStatus.setLastActiveAt(lastActiveAt);
    }

    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username;

    }

    public void updatePassword(String password) {
        validatePassword(password);

        this.password = password;

    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;

    }

//    public void updateLastActiveAt(Instant lastActiveAt) {
//        this.lastActiveAt = lastActiveAt;
//    }

    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException(ErrorType.INVALID_EMAIL);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 4) {
            throw new ValidationException(ErrorType.INVALID_PASSWORD);
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException(ErrorType.INVALID_USERNAME);
        }
    }
}
