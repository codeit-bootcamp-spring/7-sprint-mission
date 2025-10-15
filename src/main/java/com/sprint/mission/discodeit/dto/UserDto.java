package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDto extends Entity {

    private final String name;
    private final String nickname;
    private final String email;
    private final boolean isOnline;
    private final List<ChannelDto> channelDtoList;

    public UserDto(String name, String nickname, String email, boolean isOnline) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.isOnline = isOnline;
        this.channelDtoList = new ArrayList<>();
    }


    public UserDto(UUID id, String name, String nickname, String email, boolean isOnline) {
        super(id);
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.isOnline = isOnline;
        this.channelDtoList = new ArrayList<>();
    }

    public UserDto(UUID id, String name, String nickname, String email, boolean isOnline, List<ChannelDto> channelDtoList) {
        super(id);
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.isOnline = isOnline;
        this.channelDtoList = channelDtoList;
    }

    public List<ChannelDto> getChannelDtoList() {
        return channelDtoList;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
