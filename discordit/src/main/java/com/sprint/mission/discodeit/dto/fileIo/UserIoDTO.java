package com.sprint.mission.discodeit.dto.fileIo;

import com.sprint.mission.discodeit.enums.OnlineStatus;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 파일 저장용 User DTO
 */
@Getter
public class UserIoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private Instant createdAt;
    private Instant updatedAt;
    private String userId;
    private String passwd;       // 엔터티의 passwd와 일치
    private String email;
    private String displayName;  // 엔터티의 displayName과 일치
    private String bio;          // 엔터티의 bio
    private OnlineStatus onlineStatus; // User.UserStatus.name()
    private UUID profileImageId;

    public UserIoDTO(UUID uuid, Instant createdAt, Instant updatedAt,
                     String userId, String passwd, String email, String displayName,
                     String bio, OnlineStatus onlineStatus, UUID profileImageId) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.passwd = passwd;
        this.email = email;
        this.displayName = displayName;
        this.bio = bio;
        this.onlineStatus = onlineStatus;
        this.profileImageId = profileImageId;
    }

}
