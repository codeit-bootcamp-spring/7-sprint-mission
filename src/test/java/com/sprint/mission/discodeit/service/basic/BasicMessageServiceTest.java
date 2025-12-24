package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.message.InvalidMessageRequestException;
import com.sprint.mission.discodeit.common.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.page.PageResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {
    @InjectMocks
    BasicMessageService messageService;
    
    @Mock
    MessageRepository messageRepository;
    
    @Mock
    MessageMapper messageMapper;

    @Mock
    ChannelRepository channelRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @Mock
    BinaryContentService binaryContentService;

    @Spy
    PageResponseMapper pageResponseMapper = new PageResponseMapper();

    @Test
    @DisplayName("create 성공: Private 채널일 때 메세지 생성")
    void create_success() {
        // given
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        MessageCreateRequestDto messageCreateRequestDto
                = new MessageCreateRequestDto("content", authorId, channelId);

        Channel mockChannel = mock(Channel.class);
        given(mockChannel.isPrivateChannel()).willReturn(true);
        given(mockChannel.getSlowModeSeconds()).willReturn(0);

        User author = new User(
                "username", "aldldld2222", "user@naver.com", null);
        setId(author, authorId);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(mockChannel));
        given(userRepository.findById(authorId)).willReturn(Optional.of(author));
        given(readStatusRepository.existsByUserIdAndChannelId(authorId, channelId)).willReturn(true);

        Message mockMessage = mock(Message.class);
        given(messageRepository.save(any(Message.class))).willReturn(mockMessage);

        UserStatus mockUserStatus = mock(UserStatus.class);
        given(mockUserStatus.isOnlineNow()).willReturn(true);
        given(userStatusRepository.findByUserId(authorId)).willReturn(Optional.of(mockUserStatus));

        MessageResponseDto mockResponse = mock(MessageResponseDto.class);
        given(messageMapper.toDto(mockMessage, true)).willReturn(mockResponse);

        // when
        MessageResponseDto messageResponseDto = messageService.create(messageCreateRequestDto, null);

        // then
        assertThat(messageResponseDto).isSameAs(mockResponse);

        then(channelRepository).should().findById(channelId);
        then(userRepository).should().findById(authorId);
        then(readStatusRepository).should().existsByUserIdAndChannelId(authorId,channelId);
        then(messageRepository).should().save(any(Message.class));
        then(messageMapper).should().toDto(mockMessage, true);
        then(binaryContentService).shouldHaveNoInteractions();
        
    }

    @Test
    @DisplayName("create 실패: channel이 없으면 exception 발생")
    void create_fail_channelNotFound() {
        // given
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        MessageCreateRequestDto messageCreateRequestDto
                = new MessageCreateRequestDto("content", authorId, channelId);

        // when & then
        assertThatThrownBy(() -> messageService.create(messageCreateRequestDto, null))
                .isInstanceOf(ChannelNotFoundException.class);

        then(messageRepository).shouldHaveNoInteractions();

    }

    @Test
    @DisplayName("update 성공: 새로운 내용이 있다면 저장 후 dto 반환")
    void update_success() {
        // given
        UUID messageId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        User author = new User("user", "password1234", "user@naver.com", null);
        setId(author, authorId);

        Message mockMessage = mock(Message.class);
        given(mockMessage.getAuthor()).willReturn(author);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(mockMessage));
        given(messageRepository.save(any(Message.class))).willReturn(mockMessage);

        UserStatus mockStatus = mock(UserStatus.class);
        given(mockStatus.isOnlineNow()).willReturn(true);
        given(userStatusRepository.findByUserId(authorId)).willReturn(Optional.of(mockStatus));

        MessageResponseDto mockResponse = mock(MessageResponseDto.class);
        given(messageMapper.toDto(mockMessage, true)).willReturn(mockResponse);

        MessageUpdateRequestDto messageUpdateRequestDto
                = new MessageUpdateRequestDto("newContent");


        // when
        MessageResponseDto updated = messageService.update(messageId, messageUpdateRequestDto);

        // then
        assertThat(updated).isSameAs(mockResponse);
        then(messageRepository).should().findById(messageId);
        then(mockMessage).should().setContent("newContent");
        then(messageRepository).should().save(mockMessage);
        then(messageMapper).should().toDto(mockMessage, true);


    }

    @Test
    @DisplayName("update 실패: message를 찾지 못하면 exception 발생")
    void update_fail() {
        // given
        UUID messageId = UUID.randomUUID();

        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        MessageUpdateRequestDto messageUpdateRequestDto
                = new MessageUpdateRequestDto("newContent");


        // when & then
        assertThatThrownBy(() -> messageService.update(messageId, messageUpdateRequestDto))
                .isInstanceOf(MessageNotFoundException.class);

        then(messageRepository).should().findById(messageId);
        then(messageRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("delete 성공: message 삭제")
    void delete_success() {
        // given
        UUID messageId = UUID.randomUUID();
        Message mockMessage = mock(Message.class);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(mockMessage));

        // when
        boolean deleted = messageService.delete(messageId);

        // then
        assertThat(deleted).isTrue();
        then(messageRepository).should().findById(messageId);
        then(messageRepository).should().delete(mockMessage);

    }

    @Test
    @DisplayName("delete 실패: message가 없으면 exception 발생")
    void delete_fail() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.delete(messageId))
                .isInstanceOf(MessageNotFoundException.class);
        then(messageRepository).should().findById(messageId);
        then(messageRepository).should(never()).delete(any());

    }

    @Test
    @DisplayName("getPageByChannelId 성공: Slice에서 Dto로 변환")
    void getPageByChannelId_success() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0,2);

        User user1 = new User("user1", "password123", "user1@naver.com", null);
        User user2 = new User("user2", "password123", "user2@naver.com", null);
        setId(user1, authorId1);
        setId(user2, authorId2);

        Message mockMessage1 = mock(Message.class);
        Message mockMessage2 = mock(Message.class);
        given(mockMessage1.getAuthor()).willReturn(user1);
        given(mockMessage2.getAuthor()).willReturn(user2);

        SliceImpl<Message> slice
                = new SliceImpl<>(List.of(mockMessage1, mockMessage2), pageable, true);
        given(messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable)).willReturn(slice);

        UserStatus mockStatus = mock(UserStatus.class);
        given(mockStatus.isOnlineNow()).willReturn(true);

        given(userStatusRepository.findByUserId(any(UUID.class))).willReturn(Optional.of(mockStatus));

        MessageResponseDto mockResponse1 = mock(MessageResponseDto.class);
        MessageResponseDto mockResponse2 = mock(MessageResponseDto.class);
        given(messageMapper.toDto(mockMessage1, true)).willReturn(mockResponse1);
        given(messageMapper.toDto(mockMessage2, true)).willReturn(mockResponse2);

        // when
        PageResponseDto<MessageResponseDto> result = messageService.getPageByChannelId(channelId, pageable);

        // then
        assertThat(result.content()).containsExactly(mockResponse1, mockResponse2);
        assertThat(result.number()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.hasNext()).isTrue();

        then(messageRepository).should().findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
        then(messageMapper).should().toDto(mockMessage1, true);
        then(messageMapper).should().toDto(mockMessage2, true);

    }

    @Test
    @DisplayName("getPageByChannelId 실패: channelId가 null이면 exception 발생")
    void getPageByChannelId_fail_channelIdNull() {
        // then
        assertThatThrownBy(() -> messageService.getPageByChannelId(null, PageRequest.of(0, 10)))
                .isInstanceOf(InvalidMessageRequestException.class);
        then(messageRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("getPageByChannelId 실패: pageable이 null이면 exception 발생")
    void getPageByChannelId_fail_PageableNull() {
        // then
        assertThatThrownBy(() -> messageService.getPageByChannelId(UUID.randomUUID(), null))
                .isInstanceOf(InvalidMessageRequestException.class);
        then(messageRepository).shouldHaveNoInteractions();

    }

    private static void setId(Object domain, UUID id) {
        ReflectionTestUtils.setField(domain, "id", id);
    }
}
