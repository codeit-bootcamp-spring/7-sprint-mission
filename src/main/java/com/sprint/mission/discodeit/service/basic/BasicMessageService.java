package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  // ===== 🏗️ Domain Logic (Facade 용)  =====
  //레포지토리
  private final MessageRepository messageRepository;

  //메세지를 id 로 참음
  @Override
  public Message findById(UUID id) {
    return messageRepository.findById(id).orElseThrow(() ->
        new CustomException(ErrorCode.MESSAGE_NOT_FOUND));
  }

  //채널 안의 메세지들을 모두 조회
  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  //채널에서 가장 마지막 메세지를 조회
  @Override
  public Instant getListMessageTime(UUID channelId) {
    return messageRepository.findLatestCreatedAt(channelId).orElse(null);
  }

  //메세지 생성
  @Override
  public Message create(Message message) {
    return messageRepository.save(message);
  }

  //메세지 수정
  @Override
  @Transactional
  public void update(UUID id, String content, List<BinaryContent> attachments) {
    Message message = findById(id);
    message.update(content, attachments);
  }

  //메세지 삭제
  @Override
  public void delete(UUID id) {
    if (!messageRepository.existsById(id)) {
      throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
    }
    messageRepository.deleteById(id);
  }
}
