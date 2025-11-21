//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileUpdateRequestDto;
//import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
//import com.sprint.mission.discodeit.dto.response.user.UserReadResponseDto;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entityElement.UserElement;
//import com.sprint.mission.discodeit.repository.*;
//import com.sprint.mission.discodeit.service.UserService;
//import org.assertj.core.api.Assertions;
//import org.instancio.Instancio;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.instancio.Select.field;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class BasicUserServiceTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ChannelRepository channelRepository;
//
//    @Autowired
//    private MessageRepository messageRepository;
//
//    @Autowired
//    private BasicUserService basicUserService;
//
//    @Autowired
//    private BinaryContentRepository binaryContentRepository;
//
//    @Autowired
//    private UserStatusRepository userStatusRepository;
//
//    private final User DEFAULT_SENDER = User.builder().name("Default Sender").userName("DefaultSender")
//            .email("")
//            .isOnline(false)
//            .build();
//    @Autowired
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.resetUserRepository();
//        channelRepository.resetChannelRepository();
//        messageRepository.resetMessageRepository();
//        userStatusRepository.resetRepository();
//        binaryContentRepository.resetBinaryContentRepository();
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 생성")
//    void createUser() {
//        //given
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"));
//        //when
//        var actualResult = userRepository.getUserById(user.getId());
//        var userStatus = userStatusRepository.readAllUserStatus();
//        //then
//        assertThat(user).isEqualTo(actualResult.orElseThrow());
//        assertThat(userStatus.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] -프로필 이미지와 유저 생성")
//    void testCreateUserWithProfile() {
//        //given
//        var profileCreateRequestDto = new ProfileCreateRequestDto("test.png".getBytes());
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"), profileCreateRequestDto);
//
//        //when
//        var actualResultUser = userRepository.getUserById(user.getId());
//        var actualResultProfile = binaryContentRepository.readAllBinaryContent();
//        var userStatus = userStatusRepository.readAllUserStatus();
//        //then
//        assertThat(user).as("유저 객체 확인").isEqualTo(actualResultUser.orElseThrow());
//        assertThat(actualResultProfile.size()).as("프로필 객체 확인").isEqualTo(1);
//        assertThat(userStatus.size()).as("유저 스테이터스 객체 확인").isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 조회")
//    void readUser() {
//        //given
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"));
//
//        //when
//        var actualResult = basicUserService.readUser(user.getId());
//
//        //then
//        assertThat(actualResult.getUserName()).isEqualTo(user.getUserName());
//
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 삭제")
//    void deleteUser() {
//        //given
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"));
//        var channel = channelRepository.saveChannel(
//                Channel.builder().name("테스트 채널").description("테스트 채널 설명")
//                        .joinUserList(new HashSet<>(List.of(user.getId()))).build()
//        );
//
//        //when
//        basicUserService.deleteUser(user.getId());
//        var actualResultUser = userRepository.getAllUser().stream().noneMatch(u -> u.getId().equals(user.getId()));
//        var actualResultChannel = channelRepository.isChannelExit(channel.getId())&&channelRepository.getAllChannel().stream().noneMatch(c -> c.getJoinUserList().contains(user.getId()));
//        var userStatus = userStatusRepository.readAllUserStatus();
//        //then
//
//        assertThat(actualResultUser).isTrue();
//        assertThat(actualResultChannel).isTrue();
//        assertThat(userStatus.size()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 업데이트")
//    void updateUser() {
//        //given
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"));
//        //when
//        basicUserService.updateUser(new UserUpdateRequestDto<>(user.getId(), UserElement.NAME, "테스트 유저 업데이트"));
//        var actualResult = userRepository.getUserById(user.getId());
//        //then
//        assertThat(actualResult.orElseThrow().getName()).isEqualTo("테스트 유저 업데이트");
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 프로필 업데이트")
//    void testUpdateUserWithProfile() {
//        //given
//        var profileCreateRequestDto = new ProfileCreateRequestDto("test.png".getBytes());
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"), profileCreateRequestDto);
//
//        //when
//        userService.updateUser(new UserUpdateRequestDto<>(user.getId(), UserElement.NAME, "테스트 유저 업데이트"), new ProfileUpdateRequestDto("test2.png".getBytes()));
//        var actualResultUser = userRepository.getUserById(user.getId());
//        var actualResultProfile = binaryContentRepository.readAllBinaryContent();
//        //then
//        assertThat(actualResultUser.orElseThrow().getName()).isEqualTo("테스트 유저 업데이트");
//        assertThat(actualResultProfile.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 채널 입장")
//    void enterChannel() {
//        //given
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"));
//        var channel = channelRepository.saveChannel(
//                Channel.builder().name("테스트 채널").description("테스트 채널 설명")
//                        .joinUserList(new HashSet<>(List.of(user.getId()))).build()
//        );
//
//        //when
//
//        basicUserService.enterChannel(user.getId(), channel.getId());
//        var actualResultUser = userRepository.getUserById(user.getId()).orElseThrow();
//        var actualResultChannel = channelRepository.getChannelById(channel.getId()).orElseThrow();
//        //then
//        assertThat(actualResultUser.getJoinChannelList().contains(channel.getId())).as("유저의 채널 확인").isTrue();
//        assertThat(actualResultChannel.getJoinUserList().contains(user.getId())).as("채널의 유저 확인").isTrue();
//
//
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 채널 나감")
//    void exitChannel() {
//        //given
//        var user = basicUserService.createUser(new UserCreateRequestDto("testUser", "테스트 유저", "","1111"));
//        var channel = channelRepository.saveChannel(
//                Channel.builder().name("테스트 채널").description("테스트 채널 설명")
//                        .joinUserList(new HashSet<>()).build()
//        );
//        basicUserService.enterChannel(user.getId(), channel.getId());
//
//        //when
//
//        basicUserService.exitChannel(user.getId(), channel.getId());
//        var actualResultUser = userRepository.getUserById(user.getId()).orElseThrow();
//        var actualResultChannel = channelRepository.getChannelById(channel.getId()).orElseThrow();
//        //then
//        assertThat(actualResultUser.getJoinChannelList().contains(channel.getId())).as("유저의 채널 확인").isFalse();
//        assertThat(actualResultChannel.getJoinUserList().contains(user.getId())).as("채널의 유저 확인").isFalse();
//
//    }
//
//    @Test
//    @DisplayName("[예외 케이스] - 유저 이름 이메일 중복 에러")
//    void user_name_email_duplicate_error(){
//        //given
//        var user1 = basicUserService.createUser(Instancio.create(UserCreateRequestDto.class));
//        //when & then
//        assertThrows(IllegalArgumentException.class,
//                ()->basicUserService.createUser(
//                        Instancio.of(UserCreateRequestDto.class)
//                                .set(field(UserCreateRequestDto::getEmail), user1.getEmail())
//                                .create()
//                ));
//        assertThatThrownBy(()->
//                basicUserService.createUser(
//                        Instancio.of(UserCreateRequestDto.class)
//                                .set(field(UserCreateRequestDto::getUserName), user1.getUserName())
//                                .create()
//                )
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//}