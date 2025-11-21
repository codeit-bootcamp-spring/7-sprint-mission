package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.converter.BinaryContentDtoConverter;
import com.sprint.mission.discodeit.dto.converter.MessageDtoConverter;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * BasicMessageService
 * -----------------
 * MessageService 인터페이스의 구현체로,
 * Repository를 주입받아 실제 메시지 처리 로직을 수행합니다.
 *
 * - 비즈니스 로직: 메시지 생성, 필터링, 최신 메시지 조회 등
 * - 데이터 저장/조회: MessageRepository에 위임
 */
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserService userService;

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    /**
     * 새로운 메시지를 생성하여 Repository에 저장
     */
    @Override
    public MessageResponseDto create(CreateMessageRequestDto request, List<CreateBinaryContentRequestDto> binaryContentRequests) {

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 비공개 채널의 경우 유저가 채널에 속해 있는지 검증
        if (channel.getVisibility() == ChannelVisibility.PRIVATE &&
                !channel.getMemberIds().contains(request.authorId())) {
            throw new CustomException(ErrorCode.NOT_CHANNEL_MEMBER);
        }

        Message newMessage = new Message(
                request.authorId(),
                request.channelId(),
                ReceiveType.CHANNEL,
                request.content()
        );

        // 메시지에 첨부된 파일이 있는 경우에만 파일 저장
        Optional.ofNullable(binaryContentRequests).ifPresent(
                files -> files.forEach(file -> {
                    BinaryContent fileBytes = new BinaryContent(file.fileName(), file.contentType(), file.bytes());
                    newMessage.addAttachmentId(fileBytes.getId()); // 메시지에 파일 UUID 값 저장
                    binaryContentRepository.save(fileBytes);
                })
        );

        messageRepository.save(newMessage);

        return toDto(newMessage);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId).
                orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    /**
     * 특정 유저/채널과의 최신 메시지를 조회
     */
    @Override
    public Message findLastestMessage(UUID senderId, UUID receiverId, ReceiveType receiverType) {
        // 송신자 존재 여부 확인
        userRepository.findById(senderId).
                orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        List<Message> allMessages = messageRepository.findAll();

        // 최신순(역순)으로 순회
        Stream<Message> reversed = IntStream.iterate(allMessages.size() - 1, i -> i - 1)
                .limit(allMessages.size())
                .mapToObj(allMessages::get);

        // 최근 메시지는 Printer 클래스의 printChatLatest 메서드에서
        // Optional의 ifPresentOrElse를 통해 null을 저장할 경우 닉네임만 출력하기 때문에 null을 반환
        if (receiverType == ReceiveType.USER) {
            // 수신자 존재 여부 확인
            userRepository.findById(receiverId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 유저 간의 대화 중 가장 마지막 메시지
            return reversed.filter(m ->
                            (senderId.equals(m.getAuthorId()) && receiverId.equals(m.getChannelId())) ||
                                    (receiverId.equals(m.getAuthorId()) && senderId.equals(m.getChannelId())))
                    .findFirst().orElse(null);
        } else if (receiverType == ReceiveType.CHANNEL) {
            // 채널 존재 여부 확인
            channelRepository.findById(receiverId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

            // 특정 채널에 보낸 마지막 메시지
            return reversed.filter(m ->
                            senderId.equals(m.getAuthorId()) && receiverId.equals(m.getChannelId()))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }

    /**
     * 두 유저 간의 모든 메시지 목록 반환
     */
    @Override
    public List<Message> findBetweenUsers(UUID userId1, UUID userId2) {
        return messageRepository.findAll().stream()
                .filter(m -> (userId1.equals(m.getAuthorId()) && userId2.equals(m.getChannelId())) ||
                        (userId2.equals(m.getAuthorId()) && userId1.equals(m.getChannelId())))
                .toList();
    }

    /**
     * 특정 채널에 포함된 모든 메시지 조회
     */
    @Override
    public List<Message> findAllByChannelId(UUID channelId, Pageable pageable) {
        // JPA로 변경 이후 pageable 추가 필요

        return messageRepository.findAll().stream()
                .filter(m -> channelId.equals(m.getChannelId()))
                .toList();
    }

    /**
     * 유저가 보낸 메시지 조회
     */
    @Override
    public List<Message> findAllSentBetweenUsers(UUID senderId, UUID receiverId) {
        return messageRepository.findAll().stream()
                .filter(m -> senderId.equals(m.getAuthorId()) && receiverId.equals(m.getChannelId()))
                .toList();
    }

    /**
     * 메시지 내용(content) 수정
     */
    @Override
    public MessageResponseDto update(UUID messageId, UpdateMessageRequestDto request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        message.update(request.newContent());
        messageRepository.update(message);
        return toDto(message);
    }

    /**
     * 특정 메시지(UUID 기반) 삭제
     */
    @Override
    public void delete(UUID messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        binaryContentRepository.deleteByIds(msg.getAttachmentIds()); // 메시지와 관련된 파일들 삭제
        messageRepository.deleteById(messageId);
    }

    private MessageResponseDto toDto(Message message) {

        // userResponseDto 생성
        User user = userRepository.findById(message.getAuthorId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // DTO 변환을 위해 UserService의 변환 메서드 사용
        UserResponseDto userResponseDto = userService.toDto(user);

        // binaryContentDto 생성
        List<BinaryContent> attachments = null;
        List<UUID> attachmentIds = message.getAttachmentIds();
        List<BinaryContentResponseDto> binaryContentResponseDtos = null;

        // 메시지에 첨부파일이 있다면 첨부파일 리스트 저장
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            attachments = message.getAttachmentIds().stream()
                    .map(id -> binaryContentRepository.findById(id)
                            .orElseThrow(() -> new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUND)))
                    .toList();

            binaryContentResponseDtos = BinaryContentDtoConverter.toResponseDto(attachments);
        }

        return MessageDtoConverter.toResponseDto(message, userResponseDto, binaryContentResponseDtos);
    }
}
