package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.ChannelElement;
import com.sprint.mission.discodeit.entityElement.MessageElement;
import com.sprint.mission.discodeit.entityElement.UserElement;
import com.sprint.mission.discodeit.event.Listener.UserCreateListener;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.util.StaticString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static com.sprint.mission.discodeit.service.util.StaticString.*;


@SpringBootApplication
public class DiscodeitApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        UserStatusService userStatusService  = context.getBean(UserStatusService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        UserRepository userRepository = context.getBean(UserRepository.class);
        UserStatusRepository userStatusRepository = context.getBean(UserStatusRepository.class);


        User user1 = userService.createUser(new UserCreateRequestDto("testUser", "testUser", "codeit.org", "1209"));
        User user2 = userService.createUser(new UserCreateRequestDto("testUser2", "testUser2", "codeit.org", "1209"));
        Channel channel1 = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user1.getId())), "testChannel", "testChannel", true ));
        Channel channel2 = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user2.getId())), "testChannel2", "testChannel2", true ));

        Message message1 = messageService.createMessage(new MessageCreateRequestDto("Hello", user1.getId(), true, channel1.getId()));
        Message message2 = messageService.createMessage(new MessageCreateRequestDto("Hi I am Faker", user2.getId(), true, channel2.getId()));

        var userUpdateRequestDto1 = new UserUpdateRequestDto<>(user1.getId(), UserElement.NAME, "testUpdateName");
        var channelUpdateRequestDto1 = new ChannelUpdateRequestDto<>(channel1.getId(), ChannelElement.NAME, "testUpdateName");
        var messageUpdateRequestDto1 = new MessageUpdateRequestDto<>(message1.getId(), MessageElement.CONTENT, "testUpdateContent");

        System.out.println(userService.readUser(user1.getId()));
        System.out.println(channelService.readChannel(channel1.getId()));
        System.out.println(messageService.readMessage(message1));

        userRepository.saveUser(DEFAULT_SENDER);
        userStatusRepository.createUserStatus(
                UserStatus.builder()
                        .userId(DEFAULT_SENDER.getId())
                        .lastOnlineTime(Instant.now())
                        .build()
        );
        /// 생성
        System.out.println("========================업데이트 함수 확인 구역=============================");
        userService.updateUser(userUpdateRequestDto1);
        channelService.updateChannel(channelUpdateRequestDto1);
        messageService.updateMessage(messageUpdateRequestDto1);

        System.out.println(userService.readUser(user1.getId()).getName());
        System.out.println(channelService.readChannel(channel1.getId()).getName());
        System.out.println(messageService.readMessage(message1).getContent());
        System.out.println("========================삭제 함수 확인 구역=================================");

        userService.deleteUser(user1.getId());
        channelService.deleteChannel(channel1.getId());

        System.out.println(userService.readAllUser().size());
        System.out.println(messageService.readAllMessage().size());
        System.out.println(channelService.findAllByUserId(user1.getId()));
        System.out.println(readStatusService.findAllyByUserId(user2.getId()));

        System.out.println("========================Binary Content 생성============");

        var user3 = userService.createUser(new UserCreateRequestDto("testUser3", "testUser3", "codeit.org", "1209")
        , new ProfileCreateRequestDto("hello.jpg".getBytes())
        );
        System.out.println(binaryContentService.find(user3.getProfileId()));




        ///










    }

}
