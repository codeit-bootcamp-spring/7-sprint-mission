package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        // 입력값 검증 (공백, null 방지)
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("메시지 내용(content)은 비어 있을 수 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("channelId는 null일 수 없습니다.");
        }
        if (authorId == null) {
            throw new IllegalArgumentException("authorId는 null일 수 없습니다.");
        }

        // 참조 무결성 확인 (채널 및 사용자 존재 여부)
        try {
            if (!channelRepository.existsById(channelId)) {
                throw new NoSuchElementException("ID가 " + channelId + "인 채널을 찾을 수 없습니다.");
            }
            if (!userRepository.existsById(authorId)) {
                throw new NoSuchElementException("ID가 " + authorId + "인 사용자를 찾을 수 없습니다.");
            }

            // 메시지 생성 및 저장
            Message message = new Message(content, channelId, authorId);
            return messageRepository.save(message);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 재전달
            log.error("메시지 생성 중 오류 발생: channelId={}, authorId={}", channelId, authorId, e);
            throw e;
        }
    }

    @Override
    public Message find(UUID messageId) {
        // 입력값 검증
        if (messageId == null) {
            throw new IllegalArgumentException("messageId는 null일 수 없습니다.");
        }

        // 예외 처리 및 로깅
        try {
            return messageRepository.findById(messageId)
                    .orElseThrow(() -> new NoSuchElementException("ID가 " + messageId + "인 메시지를 찾을 수 없습니다."));
        } catch (Exception e) {
            log.error("메시지 조회 중 오류 발생: messageId={}", messageId, e);
            throw e;
        }
    }

    @Override
    public List<Message> findAll() {
        // 예외 처리 및 로깅
        try {
            return messageRepository.findAll();
        } catch (Exception e) {
            log.error("메시지 전체 조회 중 오류 발생", e);
            throw e;
        }
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        // 입력값 검증
        if (messageId == null) {
            throw new IllegalArgumentException("messageId는 null일 수 없습니다.");
        }
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("새 메시지 내용(newContent)은 비어 있을 수 없습니다.");
        }

        // 예외 처리 및 로깅
        try {
            Message message = messageRepository.findById(messageId)
                    .orElseThrow(() -> new NoSuchElementException("ID가 " + messageId + "인 메시지를 찾을 수 없습니다."));
            message.update(newContent);
            return messageRepository.save(message);
        } catch (Exception e) {
            log.error("메시지 수정 중 오류 발생: messageId={}", messageId, e);
            throw e;
        }
    }

    @Override
    public void delete(UUID messageId) {
        // 입력값 검증
        if (messageId == null) {
            throw new IllegalArgumentException("messageId는 null일 수 없습니다.");
        }

        // 예외 처리 및 로깅
        try {
            if (!messageRepository.existsById(messageId)) {
                throw new NoSuchElementException("ID가 " + messageId + "인 메시지를 찾을 수 없습니다.");
            }
            messageRepository.deleteById(messageId);
            log.info("메시지가 정상적으로 삭제되었습니다. id={}", messageId);
        } catch (Exception e) {
            log.error("메시지 삭제 중 오류 발생: messageId={}", messageId, e);
            throw e;
        }
    }
}
