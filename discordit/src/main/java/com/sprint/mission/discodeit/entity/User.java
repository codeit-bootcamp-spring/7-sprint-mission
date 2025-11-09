package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.config.OnlineThreshold;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.common.validator.UserValidator;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.common.config.OnlineThreshold.ONLINE_THRESHOLD;

@Getter
public class User extends BaseEntity implements Receivable {

    private final String userId;
    private String passwd;
    @Email
    private String email;

    private String displayName;
    private String bio;
    private OnlineStatus onlineStatus;
    private BinaryContent profileImage;

    public User(String userId, String passwd, String email, String displayName) {
        UserValidator.validateId(userId);
        UserValidator.validatePassword(passwd);
        UserValidator.validateDisplayName(displayName);
        UserValidator.validateEmail(email);

        this.userId = userId;
        this.passwd = passwd;
        this.email = email;
        this.displayName = displayName;
        this.onlineStatus = OnlineStatus.OFFLINE;
    }

    public OnlineStatus getOnlineStatus() {
        if (onlineStatus == OnlineStatus.ONLINE || onlineStatus == OnlineStatus.AWAY) {
            if (updatedAt.isAfter(Instant.now().minus(ONLINE_THRESHOLD))) {
                onlineStatus = OnlineStatus.ONLINE;
            } else {
                onlineStatus = OnlineStatus.AWAY;
            }
        }
        return onlineStatus;
    }

    public void setPasswd(String password) {
        UserValidator.validatePassword(password);
        this.passwd = password;
        update();
    }

    public void setDisplayName(String displayName) {
        UserValidator.validateDisplayName(displayName);
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
        if (profileImage != null) {
            user.profileImage = profileImage;
        }
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
                "userUuid='" + userId + '\'' +
                ", Passwd='" + passwd + '\'' +
                ", displayName='" + displayName + '\'' +
                ", bio='" + bio + '\'' +
                ", onlineStatus=" + onlineStatus +
                '}';
    }

}
