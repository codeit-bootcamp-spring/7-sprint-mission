package com.sprint.mission.discodeit.entity.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * 파일 저장용 User DTO
 */
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private Long createdAt;
    private Long updatedAt;
    private String userId;
    private String passwd;       // 엔터티의 passwd와 일치
    private String displayName;  // 엔터티의 displayName과 일치
    private String bio;          // 엔터티의 bio
    private String onlineStatus; // User.Status.name()

    public UserDTO(UUID uuid, Long createdAt, Long updatedAt,
                   String userId, String passwd, String displayName,
                   String bio, String onlineStatus) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.passwd = passwd;
        this.displayName = displayName;
        this.bio = bio;
        this.onlineStatus = onlineStatus;
    }

    public UUID getUuid() { return uuid; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getUserId() { return userId; }
    public String getPasswd() { return passwd; }
    public String getDisplayName() { return displayName; }
    public String getBio() { return bio; }
    public String getOnlineStatus() { return onlineStatus; }
}
