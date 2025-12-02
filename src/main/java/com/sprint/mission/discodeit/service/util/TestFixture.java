package com.sprint.mission.discodeit.service.util;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

@Service
public class TestFixture {

    public UserCreateRequestDto userCreateFactory(){
        return new UserCreateRequestDto(
                randomString(),
                randomString(),
                randomString()
        );
    }

    public UserUpdateRequest userUpdateFactory(){
        return new UserUpdateRequest(

                randomString(),
                randomString(),
                randomString()
        );
    }


    public UserStatusPatchRequestDto userStatusPatchFactory(){
        return new UserStatusPatchRequestDto(randomInstant());
    }

    public MessageCreateRequestDto messageCreateFactory(UUID userId,UUID channelId){
        return new MessageCreateRequestDto(randomString(),userId,channelId);
    }

    public ChannelPublicCreateRequestDto channelPublicCreateFactory(){
        return new ChannelPublicCreateRequestDto(randomString(),randomString());
    }

    public ChannelPatchRequestDto channelPatchFactory(){
        return new ChannelPatchRequestDto(randomString(),randomString());
    }

    public ReadStatusCreateRequestDto readStatusCreateFactory(UUID channelId,UUID userId){
        return new ReadStatusCreateRequestDto(channelId,userId,randomInstant());
    }





    private String randomString(){
        return UUID.randomUUID().toString();
    }

    private Instant randomInstant(){
        int randomDay = (int)(Math.random()*365);
        return Instant.now().minus(Duration.ofDays(randomDay));
    }

    public ReadStatusPatchRequestDto readStatusPatchFactory() {
        return new ReadStatusPatchRequestDto(randomInstant());
    }
}
