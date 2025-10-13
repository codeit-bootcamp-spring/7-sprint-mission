package com.sprint.mission.discodeit.entity;

import java.util.UUID;

/**
 * 사용자 정보를 담는 핵심 도메인 엔티티입니다.
 * 이 클래스는 사용자의 데이터뿐만 아니라, 자신의 상태를 직접 관리하는 비즈니스 로직을 포함합니다.
 * (풍부한 도메인 모델, Rich Domain Model)
 */
public class User extends BaseEntity<UUID> {

    // --- Fields ---
    /**
     * 사용자가 로그인 시 사용하는 ID (Natural Key)
     */
    private String username;
    /**
     * 암호화되어 저장되는 사용자 비밀번호
     */
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
     * 사용자의 현재 상태 (ONLINE, OFFLINE 등)
     */
    private State state;
    /**
     * 마지막으로 온라인이었던 시간 (Unix Timestamp)
     */
    private Long lastOnlineAt;

    /**
     * 외부에서 `new User()`를 통해 불완전한 객체를 생성하는 것을 막기 위해 생성자를 protected로 선언합니다.
     * 객체 생성은 반드시 `create()` 정적 팩토리 메서드를 통해서만 이루어져야 합니다.
     */
    protected User() {
        // BaseEntity의 생성자를 호출하여 공통 필드를 초기화합니다.
        super(UUID.randomUUID());
    }

    /**
     * User 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     * 필요한 모든 정보를 받아 유효성 검사를 거친 후, 완전한 상태의 객체를 생성하여 반환합니다.
     *
     * @param username    사용자 이름 (필수)
     * @param rawPassword 암호화되지 않은 비밀번호 (필수)
     * @param email       이메일 (필수)
     * @param nickname    닉네임 (선택)
     * @param phoneNum    전화번호 (선택)
     * @return 완전히 생성된 User 객체
     */
    public static User create(String username, String rawPassword, String email, String nickname, String phoneNum) {
        // Guard Clauses: 객체 생성 전 필수 값들을 검증합니다.
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        User user = new User();

        // 전달받은 인자로 필드 값을 설정합니다.
        user.username = username;
        user.password = passwordEncrypt(rawPassword);
        user.email = email;
        user.nickname = nickname;
        user.phoneNum = phoneNum;
        user.state = State.OFFLINE; // 사용자는 생성 시 기본적으로 로그인 전까지 오프라인 상태입니다.
        user.lastOnlineAt = user.getCreatedAt(); // 최초 접속 시간은 생성 시간과 동일하게 설정합니다.

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
        this.password = passwordEncrypt(newPassword);
        super.updateTimestamp();
    }

    // --- 상태 변경 비즈니스 메서드 ---

    /**
     * '온라인이 된다'는 비즈니스 이벤트를 처리합니다.
     * 상태와 마지막 접속 시간을 함께 변경하여 데이터의 일관성을 유지합니다.
     */
    public void goOnline() {
        this.state = State.ONLINE;
        this.lastOnlineAt = System.currentTimeMillis();
        super.updateTimestamp();
    }

    /**
     * '오프라인이 된다'는 비즈니스 이벤트를 처리합니다.
     */
    public void goOffline() {
        this.state = State.OFFLINE;
        super.updateTimestamp();
    }

    /**
     * '자리 비움으로 설정한다'는 비즈니스 이벤트를 처리합니다.
     * 오프라인 상태에서는 변경할 수 없다는 비즈니스 규칙을 포함합니다.
     */
    public void setAway() {
        if (this.isOffline()) {
            throw new IllegalStateException("오프라인 상태에서는 자리 비움으로 변경할 수 없습니다.");
        }
        this.state = State.AFK;
        super.updateTimestamp();
    }

    /**
     * '방해 금지로 설정한다'는 비즈니스 이벤트를 처리합니다.
     */
    public void setDoNotDisturb() {
        if (this.isOffline()) {
            throw new IllegalStateException("오프라인 상태에서는 방해 금지로 변경할 수 없습니다.");
        }
        this.state = State.DND;
        super.updateTimestamp();
    }

    // --- 상태 확인 편의 메서드 ---

    public boolean isOnline() { return this.state == State.ONLINE; }
    public boolean isOffline() { return this.state == State.OFFLINE; }
    public boolean isAway() { return this.state == State.AFK; }
    public boolean isDoNotDisturb() { return this.state == State.DND; }

    // --- private 헬퍼 메서드 ---

    /**
     * 비밀번호를 암호화하는 내부 헬퍼 메서드입니다.
     * @param password 암호화할 원본 비밀번호
     * @return 암호화된 비밀번호
     */
    private static String passwordEncrypt(String password) {
        // TODO: 실제 프로덕션에서는 bcrypt와 같은 검증된 해시 알고리즘을 사용해야 합니다.
        return "encrypted_" + password;
    }

    // --- Getters ---
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhoneNum() { return phoneNum; }
    public String getNickname() { return nickname; }
    public Long getLastOnlineAt() { return lastOnlineAt; }
    public String getPassword() { return password; }
    public State getState() { return state; }


    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() + // BaseEntity의 필드도 포함하여 출력
                ", createdAt=" + getCreatedAt() +
                ", isDeleted=" + isDeleted() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", state=" + state +
                '}';
    }
}