package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    /**
     * 새로운 메시지를 생성하여 Repository에 저장
     */
    @Override
    public void create(CreateMessageRequestDto request, List<MultipartFile> fileList) {
        Message newMessage = new Message(
                request.getSenderId(),
                request.getReceiverId(),
                request.getReceiveType(),
                request.getContent()
        );

        // 메시지에 첨부된 파일이 있는 경우에만 파일 저장
        Optional.ofNullable(fileList).ifPresent(
                files -> files.forEach(file -> {
                    try {
                        BinaryContent fileBytes = new BinaryContent(file.getBytes());
                        newMessage.addAttachmentId(fileBytes.getId()); // 메시지에 파일 UUID 값 저장
                        binaryContentRepository.save(fileBytes);
                    } catch (IOException e) {
                        throw new RuntimeException("파일 업로드 실패", e);
                    }
                })
        );

        messageRepository.save(newMessage);
    }

    /**
     * 특정 유저/채널과의 최신 메시지를 조회
     */
    @Override
    public Message findLastestMessage(UUID senderId, UUID receiverId, ReceiveType receiverType) {
        List<Message> allMessages = messageRepository.findAll();

        // 최신순(역순)으로 순회
        Stream<Message> reversed = IntStream.iterate(allMessages.size() - 1, i -> i - 1)
                .limit(allMessages.size())
                .mapToObj(allMessages::get);

        // 최근 메시지는 Printer 클래스의 printChatLatest 메서드에서
        // Optional의 ifPresentOrElse를 통해 null을 저장할 경우 닉네임만 출력하기 때문에 null을 반환
        if (receiverType == ReceiveType.USER) {
            // 유저 간의 대화 중 가장 마지막 메시지
            return reversed.filter(m ->
                            (senderId.equals(m.getSenderId()) && receiverId.equals(m.getReceiverId())) ||
                                    (receiverId.equals(m.getSenderId()) && senderId.equals(m.getReceiverId())))
                    .findFirst().orElse(null);
        } else if (receiverType == ReceiveType.CHANNEL) {
            // 특정 채널에 보낸 마지막 메시지
            return reversed.filter(m ->
                            senderId.equals(m.getSenderId()) && receiverId.equals(m.getReceiverId()))
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
                .filter(m -> (userId1.equals(m.getSenderId()) && userId2.equals(m.getReceiverId())) ||
                        (userId2.equals(m.getSenderId()) && userId1.equals(m.getReceiverId())))
                .toList();
    }

    /**
     * 특정 채널에 포함된 모든 메시지 조회
     */
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(m -> channelId.equals(m.getReceiverId()))
                .toList();
    }

    /**
     * 유저가 보낸 메시지 조회
     */
    @Override
    public List<Message> findAllSentBetweenUsers(UUID senderId, UUID receiverId) {
        return messageRepository.findAll().stream()
                .filter(m -> senderId.equals(m.getSenderId()) && receiverId.equals(m.getReceiverId()))
                .toList();
    }

    /**
     * 메시지 내용(content) 수정
     */
    @Override
    public void update(UUID messageId, UpdateMessageRequestDto request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalStateException("메시지가 존재하지 않습니다."));
        message.setContent(request.getContent());
        messageRepository.update(message);
    }

    /**
     * 특정 메시지(UUID 기반) 삭제
     */
    @Override
    public void delete(UUID messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalStateException("메시지가 존재하지 않습니다."));
        binaryContentRepository.deleteByIds(msg.getAttachmentIds()); // 메시지와 관련된 파일들 삭제
        messageRepository.deleteById(messageId);
    }
}
