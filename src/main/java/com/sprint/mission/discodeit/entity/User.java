package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserRequestDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    private final List<UUID> joinChannels = new ArrayList<>(); // 현재 참여중인 채널 리스트(UUID 리스트)

    public User(String username, String email, String password, String phoneNumber, String pronoun) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.pronoun = pronoun;
    }

    public User updateUser(UpdateUserDto updateUserDto) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.pronoun = pronoun;
        touch();

        return this;
    }

    public void addChannel(UUID channelID) {
        if (!joinChannels.contains(channelID)) {
            joinChannels.add(channelID);
            touch();
        }
    }

    public void removeChannel(UUID channelID) {
        joinChannels.remove(channelID);
        touch();
    }
}
