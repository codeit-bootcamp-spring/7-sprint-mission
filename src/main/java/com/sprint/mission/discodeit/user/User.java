package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.user.state.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * 사용자 정보를 담는 핵심 도메인 엔티티입니다.
 * 이 클래스는 사용자의 데이터뿐만 아니라, 자신의 상태를 직접 관리하는 비즈니스 로직을 포함합니다.
 * (풍부한 도메인 모델, Rich Domain Model)
 */
@Getter
@Setter
public class User extends BaseEntity<UUID> {

    // --- Fields ---
    /**
     * 사용자가 로그인 시 사용하는 ID (Natural Key)
     */
    private String username;
    /**
     * 암호화되어 저장되는 사용자 비밀번호
     */
    @Setter(AccessLevel.NONE)
    private String password;
    /**
     * 사용자 이메일 주소
     */
    private String email;
    /**
     * 사용자의 별명
     */
    private String nickname;
    /**
     * 사용자 전화번호
     */
    private String phoneNum;

    /**
     * 외부에서 `new User()`를 통해 불완전한 객체를 생성하는 것을 막기 위해 생성자를 protected로 선언합니다.
     * 객체 생성은 반드시 `create()` 정적 팩토리 메서드를 통해서만 이루어져야 합니다.
     */
    private User() {
        // BaseEntity의 생성자를 호출하여 공통 필드를 초기화합니다.
        super(UUID.randomUUID());
    }

    /**
     * User 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     * 필요한 모든 정보를 받아 유효성 검사를 거친 후, 완전한 상태의 객체를 생성하여 반환합니다.
     *
     * @param username    사용자 이름 (필수)
     * @param password 암호화되지 않은 비밀번호 (필수)
     * @param email       이메일 (필수)
     * @param nickname    닉네임 (선택)
     * @param phoneNum    전화번호 (선택)
     * @return 완전히 생성된 User 객체
     */
    public static User createUser(String username, String password, String email, String nickname, String phoneNum) {
        // Guard Clauses: 객체 생성 전 필수 값들을 검증합니다.
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        User user = new User();

        // 전달받은 인자로 필드 값을 설정합니다.
        user.username = username;
        user.password = password;
        user.email = email;
        user.nickname = nickname;
        user.phoneNum = phoneNum;

        return user;
    }

    /**
     * 사용자 프로필 정보를 수정합니다.
     * 변경된 필드가 있을 경우에만 updatedAt 타임스탬프를 갱신합니다.
     *
     * @param nickname 새 닉네임. null이거나 공백이면 변경하지 않음.
     * @param email    새 이메일. null이거나 공백이면 변경하지 않음.
     * @param phoneNum 새 전화번호. null이거나 공백이면 변경하지 않음.
     */
    public void updateProfile(String nickname, String email, String phoneNum) {
        boolean isChanged = false;

        if (nickname != null && !nickname.isBlank() && !nickname.equals(this.nickname)) {
            this.nickname = nickname;
            isChanged = true;
        }
        if (email != null && !email.isBlank() && !email.equals(this.email)) {
            this.email = email;
            isChanged = true;
        }
        if (phoneNum != null && !phoneNum.isBlank() && !phoneNum.equals(this.phoneNum)) {
            this.phoneNum = phoneNum;
            isChanged = true;
        }

        // 실제 필드 값이 변경되었을 때만 수정 시각을 갱신하여 불필요한 업데이트를 방지합니다.
        if (isChanged) {
            super.updateTimestamp();
        }
    }

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param newPassword 새로운 비밀번호
     */
    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        this.password = newPassword;
        super.updateTimestamp();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() + // BaseEntity의 필드도 포함하여 출력
                ", createdAt=" + getCreatedAt() +
                ", isDeleted=" + isDeleted() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}