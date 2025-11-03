package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    public Instant updateAt;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String pronoun; // 대명사
    private UUID profileId;

    @ToString.Exclude
    private final List<Channel> joinChannels;

    public User(String username, String email, String password, String phoneNumber, String pronoun, UUID profileId) {
        this.updateAt = Instant.now();
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.pronoun = pronoun;
        this.profileId = profileId;
        joinChannels = new ArrayList<>();
    }

    public void updateUser(String username, String password, String email, String phoneNumber, String pronoun, UUID profileId) {
        boolean isUpdate = false;
        if (username != null && !this.username.equals(username)) {
            this.username = username;
            isUpdate = true;
        }
        if (password != null && !this.password.equals(password)) {
            this.password = password;
            isUpdate = true;
        }
        if (email != null && !this.email.equals(email)) {
            this.email = email;
            isUpdate = true;
        }
        if (phoneNumber != null && !this.phoneNumber.equals(phoneNumber)) {
            this.phoneNumber = phoneNumber;
            isUpdate = true;
        }
        if (pronoun != null && !this.pronoun.equals(pronoun)) {
            this.pronoun = pronoun;
            isUpdate = true;
        }
        if (profileId != null && !this.profileId.equals(profileId)) {
            this.profileId = profileId;
            isUpdate = true;
        }

        if(isUpdate) updateAt = Instant.now();
    }

    public void joinChannel(Channel channel) {
        if (channel != null && !joinChannels.contains(channel)) {
            joinChannels.add(channel);
            updateAt = Instant.now();
        }
    }

    public void leaveChannel(Channel channel) {
        if(channel != null && joinChannels.contains(channel)) {
            joinChannels.remove(channel);
            updateAt = Instant.now();
        }
    }
}
