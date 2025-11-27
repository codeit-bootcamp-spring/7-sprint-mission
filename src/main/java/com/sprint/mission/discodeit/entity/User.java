package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.regex.Pattern;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @JoinColumn(name = "profile_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", orphanRemoval = true,
            cascade = CascadeType.ALL, optional = false)
    private UserStatus status;

    // regular expression
    private static final Pattern REGEX_EMAIL_PATTERN
            = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern REGEX_PASSWORD_PATTERN
            = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");

    @Builder
    public User(String email, String password, String username) {
        validateEmail(email);
        validatePassword(password);
        validateUserName(username);

        this.username = username;       // 특수문자 불가
        this.email = email;             // 받을때 @ 있는지 확인
        this.password = password;       // 8자리 이상
        this.profile = null;
    }

    // Update
    public void updateUsername(String username) {
        validateUserName(username);
        this.username = username;
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public void updatePassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    // 논리 삭제
    public void softDelete() {
        this.username = "Deleted User";
        this.email = this.id.toString() + "@deleted.user";
        this.password = this.id.toString();
    }

    // 유효성 검사
    private void validateEmail(String email) {
        if (email == null || !REGEX_EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("이메일 형식이 올바르지 않음");
        }
    }

    private void validatePassword(String password) {
        if (password == null || !REGEX_PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidInputException("비밀번호 형식이 올바르지 않음");
        }
    }

    private void validateUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new InvalidInputException("닉네임 형식이 올바르지 않음");
        }
    }
}