package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.login.LoginRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 참고사항: 유저와 채널 엔티티 사이에서는 M:N 관계를 형성하였지만, 메세지는 따로 아직 하지 않았습니다.
 * 채널을 삭제하면, 해당 채널에 메세지 내역은 어떻게 할 것인가
 * 유저를 삭제하면, 해당 메세지는 어떻게 할 것인가
 * 채널은 해당 채널에 대한 메세지를 들고 있을 것인가 -> 들고 있지 않기로 결정 (채널마다 메세지 리스트를 가지고 있으면, 성능 저하)
 */
@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        final UserRepository userRepository = context.getBean(UserRepository.class);
        final MessageRepository messageRepository = context.getBean(MessageRepository.class);
        final ChannelRepository channelRepository = context.getBean(ChannelRepository.class);
        final ReadStatusRepository readStatusRepository = context.getBean(ReadStatusRepository.class);
        final BinaryContentRepository binaryContentRepository = context.getBean(BinaryContentRepository.class);
        final UserStatusRepository userStatusRepository = context.getBean(UserStatusRepository.class);

        final UserService userService = context.getBean(UserService.class);
        final ChannelService channelService = context.getBean(ChannelService.class);
        final MessageService messageService = context.getBean(MessageService.class);
        final ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        final BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        final UserStatusService userStatusService = context.getBean(UserStatusService.class);
        final AuthService authService = context.getBean(AuthService.class);
        User user1 = userService.createUser(new CreateUserDto(
                "abc",
                "abc123",
                "abc@naver.com",
                "010-1111-2222",
                "abc입니다.",
                null
        ));
        User user2 = userService.createUser(new CreateUserDto(
                "ghi",
                "ghi123",
                "ghi@daum.net",
                "010-5555-6666",
                "ghi입니다.",
                null
        ));
        User user3 = userService.createUser(new CreateUserDto(
                "def",
                "def123",
                "def@gmail.com",
                "010-3333-4444",
                "def입니다.",
                null
        ));
        List<UserResponseDto> allUsers = userService.getAllUsers();
        allUsers.stream().forEach(u -> {
            System.out.println("u = " + u.toString());
        });

        Channel channel1 = channelService.createChannel(new PublicChannelDto("공지방1", "공지방1 입니다."));
        Channel channel2 = channelService.createChannel(new PrivateChannelDto(List.of(user1.getId(), user2.getId())));
        Channel channel3 = channelService.createChannel(new PrivateChannelDto(List.of(user1.getId(), user3.getId())));

        // public은 전체, private은 user2가 참여한 channel2만 조회되어야함
        List<ChannelResponseDto> allChannelByUserId = channelService.getAllChannelByUserId(user2.getId());
        System.out.println("allChannelByUserId = " + allChannelByUserId.toString());

        System.out.println("getChannel1 = " + channelService.getChannel(channel1.getId()).toString());

        CreateBinaryContentDto createBinaryContentDto = new CreateBinaryContentDto("test12", "text", new byte[10]);
        List<CreateBinaryContentDto> list = new ArrayList<>();
        list.add(createBinaryContentDto);
        messageService.createMessage(new CreateMessageDto("user1이 channel2에서 보내는 메세지",user1.getId(), channel2.getId(), list));
        messageService.createMessage(new CreateMessageDto("user1이 channel3에서 보내는 메세지",user1.getId(), channel3.getId(), null));
        messageService.createMessage(new CreateMessageDto("user3이 channel1에서 보내는 메세지",user3.getId(), channel1.getId(), null));

        System.out.println("=========== 메세지 테스트 ==================");
        List<Message> allMessages = messageService.getAllMessages();
        allMessages.stream().forEach(u -> {
            System.out.println("u = " + u.toString());
            });

        System.out.println("=========== 각 유저에 참여 채널리스트 ==================");
        List<UserResponseDto> allUsers2 = userService.getAllUsers();
        allUsers.forEach(u -> {
            System.out.println("u = " + u.getJoinChannels().toString());
        });

        System.out.println("======== BinaryContent 데이터 확인 ===");
        for (BinaryContent binaryContent : binaryContentRepository.findAll()) {
            System.out.println("binaryContent = " + binaryContent.toString());
        }


        authService.login(new LoginRequestDto("def", "def123"));
//        authService.login(new LoginRequestDto("ghi", "aaa")); // 로그인 실패 테스트

        System.out.println("============ ReadStatus 확인 ======================");
        List<ReadStatus> all = readStatusRepository.findAll();
        all.stream().forEach(u -> {
            System.out.println("u = " + u.toString());
        });

        System.out.println("========== 채널 삭제 시 user, message 확인====");
        channelService.deleteChannel(channel1.getId());
        System.out.println("유저");
        userService.getAllUsers().stream().forEach(u -> {
            System.out.println("u = " + u.toString());
        });
        System.out.println("메세지");
        messageService.getAllMessages().stream().forEach(u -> {
            System.out.println("u = " + u.toString());
        });
        

    }

}
