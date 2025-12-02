package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.util.TestFixture;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Autowired
    TestFixture fixture;

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

    @Test
    @DisplayName("[정상 케이스] 메세지 생성")
    void createMessage() throws IOException {
        //given
        UserDto user = userService.createUser(fixture.userCreateFactory(), null);
        ChannelDto publicChannel = channelService.createPublicChannel(fixture.channelPublicCreateFactory());
        MessageCreateRequestDto message = fixture.messageCreateFactory(user.id(), publicChannel.id());

        //when

        MessageDto message1 = messageService.createMessage(message, null);

        //then
        var actualResult = messageRepository.findById(message1.id()).orElseThrow().getContent();
        var expectedResult = message.content();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[예외 케이스] null 값 넣기")
    void createInvalidChannel() throws IOException {

        MessageCreateRequestDto messageCreateRequestDto1 = fixture.messageCreateFactory(null, null);

        assertThrows(InvalidDataAccessApiUsageException.class,()->messageService.createMessage(messageCreateRequestDto1,
                null));

    }

    @Test
    @DisplayName("[예외 케이스] 채널에 없는 유저 메세지 생성")
    void createMessageWithNotJoinUser() throws IOException {

        UserDto user = userService.createUser(fixture.userCreateFactory(), null);
        UserDto user2 = userService.createUser(fixture.userCreateFactory(), null);
        ChannelDto privateChannel = channelService.createPrivateChannel(
                new ChannelPrivateCreateRequestDto(new HashSet<>(List.of(user2.id())))

        );

        MessageCreateRequestDto messageCreateRequestDto2 = fixture.messageCreateFactory(user.id(), privateChannel.id());

        assertThrows(IllegalArgumentException.class,()->messageService.createMessage(
                        messageCreateRequestDto2,
                        null
                )
        );
    }


    @Test
    @DisplayName("[정상 케이스] 메세지 전체 조회")
    void readAllMessage() {
        //given

        //when

        //then
        var actualResult = messageService.readAllMessage().size();
        var expectedResult = messageRepository.findAll().size();
        assertEquals(expectedResult,actualResult);
    }


    @Test
    @DisplayName("[정상 케이스] 메세지 삭제")
    void deleteMessage() throws IOException {
        //given

        UserDto user = userService.createUser(fixture.userCreateFactory(), null);
        ChannelDto publicChannel = channelService.createPublicChannel(fixture.channelPublicCreateFactory());
        MessageCreateRequestDto message = fixture.messageCreateFactory(user.id(), publicChannel.id());
        MessageDto message1 = messageService.createMessage(message, null);
        //when
        messageService.deleteMessage(message1.id());

        //then
        var actualResult = messageRepository.findById(message1.id()).isPresent();
        var expectedResult = false;
        assertEquals(expectedResult,actualResult);
    }



    @Test
    @DisplayName("[정상 케이스] 메세지 변경")
    void patchMessage() throws IOException {
        //given
        UserDto user = userService.createUser(fixture.userCreateFactory(), null);
        ChannelDto publicChannel = channelService.createPublicChannel(fixture.channelPublicCreateFactory());
        MessageCreateRequestDto message = fixture.messageCreateFactory(user.id(), publicChannel.id());
        MessageDto message1 = messageService.createMessage(message, null);
        String newContent = "newContent";
        //when
        messageService.patchMessage(new MessagePatchRequestDto(newContent),message1.id());

        //then
        var actualResult = messageRepository.findById(message1.id()).orElseThrow().getContent();
        var expectedResult = newContent;
        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] 메세지 파일 생성")
    void createMessageAttachment() throws IOException {

    //given
        UserDto user = userService.createUser(fixture.userCreateFactory(), null);
        ChannelDto publicChannel = channelService.createPublicChannel(fixture.channelPublicCreateFactory());
        MessageCreateRequestDto message = fixture.messageCreateFactory(user.id(), publicChannel.id());

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
        var actualResult = binaryContentDto!=null;
        var expectedResult = true;
        assertEquals(expectedResult,actualResult);

    }

    @Test
    @DisplayName("[예외 케이스] 존재하지 않는 메세지 삭제")
    void deleteNotMessage() throws IOException {

        //delete 는 기본적으로 에러를 뱉지 않는다.
        assertDoesNotThrow( () ->messageService.deleteMessage(UUID.randomUUID()));
    }
}