package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


public class TestFixture {

    public static  UserCreateRequestDto userCreateFactory(){
        return new UserCreateRequestDto(
                randomString(),
                randomString(),
                randomString()
        );
    }

    public static UserUpdateRequest userUpdateFactory(){
        return new UserUpdateRequest(

                randomString(),
                randomString(),
                randomString()
        );
    }


    public static UserStatusPatchRequestDto userStatusPatchFactory(){
        return new UserStatusPatchRequestDto(randomInstant());
    }

    public static MessageCreateRequestDto messageCreateFactory(UUID userId,UUID channelId){
        return new MessageCreateRequestDto(randomString(),userId,channelId);
    }

    public static ChannelPublicCreateRequestDto channelPublicCreateFactory(){
        return new ChannelPublicCreateRequestDto(randomString(),randomString());
    }

    public static ChannelPatchRequestDto channelPatchFactory(){
        return new ChannelPatchRequestDto(randomString(),randomString());
    }

    public static ReadStatusCreateRequestDto readStatusCreateFactory(UUID channelId,UUID userId){
        return new ReadStatusCreateRequestDto(channelId,userId,randomInstant());
    }





    static String randomString(){
        return UUID.randomUUID().toString();
    }

    static Instant randomInstant(){
        int randomDay = (int)(Math.random()*365);
        return Instant.now().minus(Duration.ofDays(randomDay));
    }

    public static ReadStatusPatchRequestDto readStatusPatchFactory() {
        return new ReadStatusPatchRequestDto(randomInstant());
    }
}
