package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.exception.domain.message.MessageNotExistException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotJoinException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MessageServiceIntegrationTest {

    @Autowired
    MessageService messageService;

    @Autowired
    ChannelService channelService;

    @Autowired
    UserService userService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    BinaryContentService binaryContentService;

    @Autowired
    MessageAttachmentRepository messageAttachmentRepository;


    @BeforeEach
    void setUp() {

    }

    @Nested
    @DisplayName("메세지 생성 테스트")
    class MessageCreateTest {
        @Test
        @DisplayName("[정상 케이스] 메세지 생성")
        @Transactional
        void createMessage() throws IOException {
            //given
            UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
            ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
            MessageCreateRequestDto message = TestFixture.messageCreateFactory(user.id(), publicChannel.id());

            //when

            MessageDto message1 = messageService.createMessage(message, null);

            //then
            var actualResult = messageRepository.findById(message1.id()).orElseThrow().getContent();
            var expectedResult = message.content();
            assertEquals(expectedResult, actualResult);
        }

        @Test
        @DisplayName("[예외 케이스] null 값 넣기")
        @Transactional
        void createInvalidChannel() throws IOException {

            MessageCreateRequestDto messageCreateRequestDto1 = TestFixture.messageCreateFactory(null, null);

            assertThrows(InvalidDataAccessApiUsageException.class, () -> messageService.createMessage(messageCreateRequestDto1,
                    null));

        }

        @Test
        @DisplayName("[예외 케이스] 채널에 없는 유저 메세지 생성")
        @Transactional
        void createMessageWithNotJoinUser() {

            UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
            UserDto user2 = userService.createUser(TestFixture.userCreateFactory(), null);
            ChannelDto privateChannel = channelService.createPrivateChannel(
                    new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user2.id())))

            );

            MessageCreateRequestDto messageCreateRequestDto2 = TestFixture.messageCreateFactory(user.id(), privateChannel.id());

            assertThatThrownBy(() -> messageService.createMessage(messageCreateRequestDto2, null))
                    .isInstanceOf(UserNotJoinException.class);
        }

    }

    @Nested
    @DisplayName("메세지 변경 테스트")
    class MessagePatchTest {

        @Test
        @DisplayName("[정상 케이스] 메세지 변경")
        @Transactional
        void patchMessage() {
            //given
            UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
            ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
            MessageCreateRequestDto message = TestFixture.messageCreateFactory(user.id(), publicChannel.id());
            MessageDto message1 = messageService.createMessage(message, null);
            String newContent = "newContent";
            //when
            messageService.patchMessage(new MessagePatchRequestDto(newContent), message1.id());

            //then
            var actualResult = messageRepository.findById(message1.id()).orElseThrow().getContent();
            var expectedResult = newContent;
            assertEquals(expectedResult, actualResult);
        }

        @Test
        @DisplayName("[정상 케이스] 메세지 파일 생성")
        @Transactional
        void createMessageAttachment() throws IOException {

            //given
            UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
            ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
            MessageCreateRequestDto message = TestFixture.messageCreateFactory(user.id(), publicChannel.id());

            MultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    "test.txt",
                    "text/plain",
                    new byte[0]   // 빈 파일
            );

            //when
            MessageDto message1 = messageService.createMessage(message, List.of(multipartFile));
            MessageAttachment messageAttachment = messageAttachmentRepository.findAll().stream().
                    filter(x -> x.getMessage().getId()
                            .equals(message1.id())).findFirst().orElseThrow();
            BinaryContentDto binaryContentDto = binaryContentService.find(messageAttachment.getBinaryContent().getId());
            //then
            var actualResult = binaryContentDto != null;
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);

        }

        @Test
        @DisplayName("[예외 케이스] 메세지 변경 실패")
        @Transactional
        void patchNotExistMessage_Fail() {

            assertThatThrownBy(() -> messageService.patchMessage(new MessagePatchRequestDto("newContent")
                    , UUID.randomUUID())).isInstanceOf(MessageNotExistException.class);
        }

    }

    @Nested
    @DisplayName("메세지 조회 테스트")
    class MessageReadTest {


        @Test
        @DisplayName("[정상 케이스] 메세지 전체 조회")
        @Transactional
        void readAllMessage() {
            //given

            //when

            //then
            var actualResult = messageService.readAllMessage().size();
            var expectedResult = messageRepository.findAll().size();
            assertEquals(expectedResult, actualResult);
        }


        @Test
        @DisplayName("[정상 케이스] 메세지 커서 페이지네이션 조회 성공")
        @Transactional
        void readCursorMessage_Success() {

            //given
            UserDto user = userService.createUser
                    (TestFixture.userCreateFactory(), null);
            ChannelDto publicChannel = channelService.createPublicChannel
                    (TestFixture.channelPublicCreateFactory());

            MessageDto message1 = messageService.createMessage(TestFixture.messageCreateFactory
                    (user.id(), publicChannel.id()), null);
            MessageDto message2 = messageService.createMessage(TestFixture.messageCreateFactory
                    (user.id(), publicChannel.id()), null);
            //when
            PageResponseDto<MessageDto> actualResult = messageService.findallByChannelIdWithCursor(
                    publicChannel.id(),
                    null,
                    Pageable.ofSize(10)
            );

            assertThat(actualResult.totalElements()).isEqualTo(2);
            assertThat(actualResult.content().stream().map(MessageDto::id))
                    .contains(message1.id(), message2.id());

            //then

        }

        @Test
        @DisplayName("[예외 케이스] 메세지 커서 페이지네이션 조회 실패 (페이지 사이즈에 음수 넣기)")
        @Transactional
        void readCursorMessage_Fail() {
            //given
            UserDto user = userService.createUser
                    (TestFixture.userCreateFactory(), null);
            ChannelDto publicChannel = channelService.createPublicChannel
                    (TestFixture.channelPublicCreateFactory());

            MessageDto message1 = messageService.createMessage(TestFixture.messageCreateFactory
                    (user.id(), publicChannel.id()), null);
            MessageDto message2 = messageService.createMessage(TestFixture.messageCreateFactory
                    (user.id(), publicChannel.id()), null);

            //when
            assertThatThrownBy(
                    () -> messageService.findallByChannelIdWithCursor(
                            publicChannel.id(),
                            null,
                            Pageable.ofSize(-5)
                    )
            ).isInstanceOf(IllegalArgumentException.class);


        }

    }

    @Nested
    @DisplayName("메세지 삭제 테스트")
    class MessageDeleteTest {


        @Test
        @DisplayName("[정상 케이스] 메세지 삭제")
        @Transactional
        void deleteMessage() throws IOException {
            //given

            UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
            ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
            MessageCreateRequestDto message = TestFixture.messageCreateFactory(user.id(), publicChannel.id());
            MessageDto message1 = messageService.createMessage(message, null);
            //when
            messageService.deleteMessage(message1.id());

            //then
            var actualResult = messageRepository.findById(message1.id()).isPresent();
            var expectedResult = false;
            assertEquals(expectedResult, actualResult);
        }

        @Test
        @DisplayName("[예외 케이스] 존재하지 않는 메세지 삭제")
        @Transactional
        void deleteNotMessage() throws IOException {

            assertThatThrownBy(() -> messageService.deleteMessage(UUID.randomUUID()))
                    .isInstanceOf(MessageNotExistException.class);
        }

    }

}