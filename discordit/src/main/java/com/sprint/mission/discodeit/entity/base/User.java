package com.sprint.mission.discodeit.entity.base;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import jakarta.validation.constraints.Email;
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
    @Email
    private String email;

    private String displayName;
    private String bio;
    private OnlineStatus onlineStatus;
    private BinaryContent profileImage;

    public User(String userId, String passwd, String email, String displayName) {
        validateId(userId);
        validatePasswd(passwd);
        validateDisplayName(displayName);

        this.userId = userId;
        this.passwd = passwd;
        this.email = email;
        this.displayName = displayName;
        this.onlineStatus = OnlineStatus.OFFLINE;
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

    public void setProfileImage(BinaryContent profileImage) {
        this.profileImage = profileImage;
        update();
    }

    public void setOnlineStatus(OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
        update();
    }

    public void setEmail(String email) {
        this.email = email;
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
                               String userId, String passwd, String email, String displayName,
                               String bio, OnlineStatus OnlineStatus, BinaryContent profileImage){
        User user = new User(userId, passwd, email, displayName);
        user.uuid = uuid;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        user.bio = bio;
        user.onlineStatus = OnlineStatus;
        user.profileImage = profileImage;
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

}
