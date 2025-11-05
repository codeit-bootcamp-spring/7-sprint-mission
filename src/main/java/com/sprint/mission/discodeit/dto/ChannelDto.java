package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChannelDto extends Entity{

    private final String name;
    private final String description;
    private final boolean isPublic;
    private final boolean isTextChannel;
    private final List<UserDto> userDtoList;



    public ChannelDto(String name, String description, boolean isPublic, boolean isTextChannel) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
        this.userDtoList = new ArrayList<>();
    }

    public ChannelDto(String name, String description, boolean isPublic, boolean isTextChannel, List<UserDto> userDtoList) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
        this.userDtoList = userDtoList;
    }
    public ChannelDto(UUID uuid, String name, String description, boolean isPublic, boolean isTextChannel) {
        super(uuid);
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
        this.userDtoList = new ArrayList<>();
    }

    public ChannelDto(UUID uuid, String name, String description, boolean isPublic, boolean isTextChannel, List<UserDto> userDtoList) {
        super(uuid);
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
        this.userDtoList = userDtoList;
    }

    @Override
    public String toString() {
        return "ChannelDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPublic +
                ", isTextChannel=" + isTextChannel +
                ", userDtoList=" + userDtoList.stream().map(UserDto::getName).reduce("", (a, b) -> a + "\n" + b)+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChannelDto that)) return false;
        return isPublic == that.isPublic && isTextChannel == that.isTextChannel && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, isPublic, isTextChannel);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isTextChannel() {
        return isTextChannel;
    }

    public List<UserDto> getUserDtoList() {
        return userDtoList;
    }


}
