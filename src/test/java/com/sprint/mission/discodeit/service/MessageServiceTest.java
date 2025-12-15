package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentManager;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.mapper.MessageMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService 테스트")
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private MessageAttachmentRepository attachmentRepository;
    @Mock
    private MessageMapper mapper;
    @Spy
    private BinaryContentMapper binaryContentMapper = Mappers.getMapper(BinaryContentMapper.class);
    @Mock
    private BinaryContentManager binaryContentManager;

    @InjectMocks
    private MessageService messageService;

    @Nested
    @DisplayName("메세지 생성")
    class createMessage {
        @Test
        @DisplayName("메세지 생성 성공 - 파일 X")
        void createMessageSuccess_noFile() {
            //given
            User user = new User("test@gmail.com", "1234", "test");
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();

            given(userRepository.findById(userId))
                    .willReturn(Optional.of(user));
            given(channelRepository.findById(channelId))
                    .willReturn(Optional.of(channel));

            MessageCreateRequest request = new MessageCreateRequest("test content", userId, channelId);
            Message message = new Message(user, channel, request.content());

            given(messageRepository.save(any(Message.class)))
                    .willReturn(message);

            MessageDto messageDto = new MessageDto();
            given(mapper.toDto(message))
                    .willReturn(messageDto);

            //when
            messageService.sendMessage(request, List.of());

            //then
            then(userRepository).should().findById(userId);
            then(channelRepository).should().findById(channelId);
            then(messageRepository).should().save(any(Message.class));

            assertThat(message.getChannel()).isEqualTo(channel);

            then(binaryContentManager).should(never()).saveFileAndMeta(any());
            then(attachmentRepository).should(never()).save(any());
            then(binaryContentMapper).should(never()).toDto(any());
        }

        @Test
        @DisplayName("메세지 생성 성공 - 파일 O")
        void createMessageSuccess_file() {
            //given
            User user = new User("test@gmail.com", "1234", "test");
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();

            given(userRepository.findById(userId))
                    .willReturn(Optional.of(user));
            given(channelRepository.findById(channelId))
                    .willReturn(Optional.of(channel));

            MessageCreateRequest request = new MessageCreateRequest("test content", userId, channelId);
            Message message = new Message(user, channel, request.content());

            given(messageRepository.save(any(Message.class)))
                    .willReturn(message);

            MessageDto messageDto = new MessageDto();
            given(mapper.toDto(message))
                    .willReturn(messageDto);

            MockMultipartFile file = new MockMultipartFile(
                    "files",                  // 파라미터 이름
                    "test.txt",               // 파일명
                    "text/plain",             // content type
                    "hello world".getBytes()  // 내용
            );
            List<MultipartFile> files = List.of(file);
            BinaryContent binaryContent = new BinaryContent("name", "png", 1234);
            given(binaryContentManager.saveFileAndMeta(any(MultipartFile.class)))
                    .willReturn(binaryContent);

            MessageAttachment messageAttachment = new MessageAttachment(message, binaryContent);
            given(attachmentRepository.save(any(MessageAttachment.class)))
                    .willReturn(messageAttachment);
            BinaryContentDto binaryContentDto = new BinaryContentDto();
            given(binaryContentMapper.toDto(binaryContent))
                    .willReturn(binaryContentDto);

            //when
            messageService.sendMessage(request, files);

            //then
            then(userRepository).should().findById(userId);
            then(channelRepository).should().findById(channelId);
            then(messageRepository).should().save(any(Message.class));

            assertThat(message.getChannel()).isEqualTo(channel);
            assertThat(messageDto.getAttachments()).contains(binaryContentDto);
            then(binaryContentManager).should().saveFileAndMeta(any(MultipartFile.class));
            then(attachmentRepository).should().save(any(MessageAttachment.class));
            then(binaryContentMapper).should().toDto(any(BinaryContent.class));
        }

        @Test
        @DisplayName("메세지 생성 실패")
        void createMessageFail() {
            //given
            User user = new User("test@gmail.com", "1234", "test");
            UUID userId = UUID.randomUUID();
            given(userRepository.findById(userId))
                    .willReturn(Optional.empty());

            MessageCreateRequest request = new MessageCreateRequest("test content", userId, UUID.randomUUID());

            //when
            assertThatThrownBy(() -> messageService.sendMessage(request, List.of()))
                    .isInstanceOf(UserNotFoundException.class);

            //then
            then(userRepository).should().findById(userId);
            then(channelRepository).should(never()).findById(any());
            then(messageRepository).should(never()).save(any(Message.class));
            then(binaryContentManager).should(never()).saveFileAndMeta(any(MultipartFile.class));
            then(attachmentRepository).should(never()).save(any(MessageAttachment.class));
            then(binaryContentMapper).should(never()).toDto(any(BinaryContent.class));
        }
    }

    @Nested
    @DisplayName("메세지 수정")
    class UpdateMessage {
        @Test
        @DisplayName("메세지 수정 성공")
        void updateMessageSuccess() {
            //given
            UUID messageId = UUID.randomUUID();
            User user = new User("test@gmail.com", "1234", "test");
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);

            Message message = new Message(user, channel, "old message");

            MessageUpdateRequest request = new MessageUpdateRequest("newContent");

            given(messageRepository.findById(messageId))
                    .willReturn(Optional.of(message));

            MessageDto messageDto = new MessageDto();
            given(mapper.toDto(message))
                    .willReturn(messageDto);

            //when
            messageService.updateMessage(messageId, request);

            //then
            then(messageRepository).should().findById(messageId);
            assertThat(message.getContent()).isEqualTo(request.newContent());
        }

        @Test
        @DisplayName("메세지 수정 실패")
        void updateMessageFail() {
            //given
            UUID messageId = UUID.randomUUID();
            given(messageRepository.findById(messageId))
                    .willReturn(Optional.empty());
            MessageUpdateRequest request = new MessageUpdateRequest("newContent");

            //when
            assertThatThrownBy(() -> messageService.updateMessage(messageId, request))
                    .isInstanceOf(MessageNotFoundException.class);

            //then
            then(messageRepository).should().findById(messageId);
            then(mapper).should(never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("메세지 삭제")
    class DeleteMessage {
        @Test
        @DisplayName("메세지 삭제 성공")
        void deleteMessageSuccess() {
            //given
            UUID messageId = UUID.randomUUID();
            User user = new User("test@gmail.com", "1234", "test");
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);
            Message message = new Message(user, channel, "old message");

            BinaryContent binaryContent = new BinaryContent("name", "png", 1234);

            MessageAttachment messageAttachment = new MessageAttachment(message, binaryContent);

            given(attachmentRepository.findAllWithBinaryContentByMessageId(messageId))
                    .willReturn(List.of(messageAttachment));
            //when
            messageService.deleteMessage(messageId);

            //then
            then(attachmentRepository).should().findAllWithBinaryContentByMessageId(messageId);
            then(binaryContentManager).should().deleteFile(binaryContent);
            then(attachmentRepository).should().deleteByMessageId(messageId);
            then(messageRepository).should().deleteById(messageId);
        }

        @Test
        @DisplayName("메세지 삭제 성공2")
        void deleteMessageSuccess_noFile() {
            //given
            UUID messageId = UUID.randomUUID();
            User user = new User("test@gmail.com", "1234", "test");
            Channel channel = new Channel("test", "test", ChannelType.PUBLIC);
            Message message = new Message(user, channel, "old message");

            given(attachmentRepository.findAllWithBinaryContentByMessageId(messageId))
                    .willReturn(List.of());
            //when
            messageService.deleteMessage(messageId);

            //then
            then(attachmentRepository).should().findAllWithBinaryContentByMessageId(messageId);
            then(binaryContentManager).should(never()).deleteFile(any());
            then(attachmentRepository).should().deleteByMessageId(messageId);
            then(messageRepository).should().deleteById(messageId);
        }
    }

    @Nested
    @DisplayName("메세지 조회")
    class FindMessages {
        @Test
        @DisplayName("채널 별 메세지 조회 성공")
        void findByChannelSuccess() {
            //given


            //when
//            messageService.getAllByChannelId()

            //then

        }
    }
}
