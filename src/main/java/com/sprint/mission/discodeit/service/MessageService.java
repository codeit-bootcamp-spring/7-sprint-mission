package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

// 생성 , 읽기 , 모두읽기, 수정, 삭제
public interface MessageService {
    MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments);
    MessageResponseDto get(UUID messageId);
    List<MessageResponseDto> getAll();
    List<MessageResponseDto> getAllByChannelId(UUID channelId);
    MessageResponseDto update(UUID messageId, MessageUpdateRequestDto messageUpdateRequestDto);
    boolean delete(UUID uuid);
    List<MessageResponseDto> getMessagesByAuthor(UUID authorId);
    List<MessageResponseDto> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId);
    List<MessageResponseDto> searchByKeyword(String keyword);
}
