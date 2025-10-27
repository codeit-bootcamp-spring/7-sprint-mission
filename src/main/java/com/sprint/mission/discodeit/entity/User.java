package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Common {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String pronoun; // 대명사 (디스코드에 있음)

    @ToString.Exclude
    private final List<Channel> joinChannels = new ArrayList<>(); // 현재 참여중인 채널 리스트(UUID 리스트)

    public User(String username, String email, String password, String phoneNumber, String pronoun) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.pronoun = pronoun;
    }

    public void updateUser(String username, String password, String  email, String phoneNumber, String pronoun) {
        if(username != null) this.username = username;
        if(password != null) this.password = password;
        if(email != null) this.email = email;
        if(phoneNumber != null) this.phoneNumber = phoneNumber;
        if(pronoun != null) this.pronoun = pronoun;
        touch();
    }

    public void joinChannel(Channel channel) {
        if (channel != null && !joinChannels.contains(channel)) {
            joinChannels.add(channel);
            touch();
        }
    }

    public void leaveChannel(Channel channel) {
        if(channel != null && joinChannels.contains(channel)) {
            joinChannels.remove(channel);
            touch();
        }

    }
}
