package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserUpdateParams;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile; // TODO: 아직 로직상 profileId로 쓰고있을수도잇으니 이부분 추후 요구사항의 Profile 객체로 변경
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus; // 1:1 양방향

    @Builder
    private User(String nickname, String email, String password, BinaryContent profile) {
          /*
        엔티티의 업데이트 메서드(전이)는 항상 관련 불변식을 재확인하는 검증을 포함한다.
        이 검증이 “입력 가드처럼 보일 수” 있지만, 목적은 외부 입력 차단이 아니라 내 상태 보전이기 때문에 불변식 검사라고 부른다.
         */

        if (nickname == null || nickname.isBlank()) throw new IllegalArgumentException("nickname invalid");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("email invalid");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password invalid");
        this.username = nickname;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public static User create(String nickname, String email, String password, BinaryContent profile) {
        return new User(nickname, email, password, profile);
    }

    public void update(UserUpdateParams request) {
        // PATCH방식으로 체크, null이면 바꾸지않기
        this.updateNickname(request.nickname());
        this.updateEmail(request.email());
        this.updatePassword(request.password());
        this.updateProfile(request.profile());

    }

    private boolean updateProfile(BinaryContent profile) {
        if (profile != null) {
            this.profile = profile;
            return true;
        }
        return false;
    }

    public boolean updateNickname(String nickname) {
        if (nickname != null && !nickname.isBlank() && !nickname.equals(this.username)) {
            this.username = nickname;
            return true;
        }
        return false;

    }

    public boolean updatePassword(String password) {
        if (password != null && !password.isBlank() && !password.equals(this.password)) {
            this.password = password;
            return true;
        }
        return false;
    }

    public boolean updateEmail(String email) {
        if (email != null && !email.isBlank() && !email.equals(this.email)) {
            this.email = email;
            return true;
        }
        return false;
    }

    // 편의 메서드 추가
    public void initUserStatus() {
        if (this.userStatus == null) {
            this.userStatus = new UserStatus(this);
        }
    }

}
