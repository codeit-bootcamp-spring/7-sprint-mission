package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends BaseEntity implements Receivable {

    // 직렬화 및 역직렬화를 수행할 때 이 클래스의 버전을 의미
    private static final long serialVersionID = 1L;

    private final String userId;
    private String passwd;

    private String displayName;
    private String bio;
    private Status onlineStatus;

    public User(String userId, String passwd, String displayName) {
        validateId(userId);
        validateId(passwd);
        validateDisplayName(displayName);

        this.userId = userId;
        this.passwd = passwd;
        this.displayName = displayName;
        this.onlineStatus = Status.OFFLINE;
    }

    public void setPasswd(String passwd) {
        validatePasswd(passwd);
        this.passwd = passwd;
        update();
    }

    public void setDisplayName(String displayName) {
        validateDisplayName(displayName);
        this.displayName = displayName;
        update();
    }

    public void setBio(String bio) {
        this.bio = bio;
        update();
    }

    public void setOnlineStatus(Status onlineStatus) {
        this.onlineStatus = onlineStatus;
        update();
    }

    private static void validateId(String id){
        if (id.length() < 4 || id.length() > 10){
            throw new IllegalArgumentException("아이디는 4에서 10자 사이로 입력해주세요.");
        }
    }

    private static void validatePasswd(String passwd) {
        // 지금은 아이디와 제약사항이 같지만 나중에 변경될 수 있어 분리함
        if (passwd.length() < 4 || passwd.length() > 10){
            throw new IllegalArgumentException("비밀번호는 4에서 10자 사이로 입력해주세요.");
        }
    }

    private static void validateDisplayName(String displayName) {
        if (displayName.length() > 10){
            throw new IllegalArgumentException("닉네임은 10자 이하로 입력되어야 합니다. 입력 글자 수 :" + displayName.length());
        }
    }

    // 파일 IO시 필드 복원을 위한 메서드
    public static User fromDto(UUID uuid, Instant createdAt, Instant updatedAt,
                               String userId, String passwd, String displayName,
                               String bio, Status status){
        User user = new User(userId, passwd, displayName);
        user.uuid = uuid;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        user.bio = bio;
        user.onlineStatus = status;
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", Passwd='" + passwd + '\'' +
                ", displayName='" + displayName + '\'' +
                ", bio='" + bio + '\'' +
                ", onlineStatus=" + onlineStatus +
                '}';
    }

    public enum Status{
        ONLINE("온라인"),
        OFFLINE("오프라인"),
        AWAY("자리비움"),
        DO_NOT_DISTURB("방해금지");

        Status(String description){
        }
    }

}
