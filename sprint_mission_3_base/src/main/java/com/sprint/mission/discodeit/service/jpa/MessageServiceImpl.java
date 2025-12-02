package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final ChannelRepository channelRepo;
    private final BinaryContentRepository binaryRepo;
    private final BinaryContentStorage storage;  // 파일 저장

    @Override
    public MessageDto createWithFile(MessageCreateRequest request, MultipartFile file) {

        User user = userRepo.findById(request.authorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Channel channel = channelRepo.findById(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        // 1) 메시지 저장
        Message message = Message.builder()
                .content(request.content())
                .user(user)
                .channel(channel)
                .build();
        messageRepo.save(message);

        // 2) 이미지 있으면 저장
        if (file != null && !file.isEmpty()) {
            try {
                BinaryContent binary = BinaryContent.builder()
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getBytes().length)
                        .message(message)  // 메시지와 연결
                        .build();

                binaryRepo.save(binary);
                storage.put(binary.getId(), file.getBytes());

                // 메시지가 binaryContent를 인식하게 설정
                message.updateBinary(binary);

            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }
        }

        return MessageDto.from(message);
    }

    @Override
    public MessageDto update(MessageUpdateRequest request) {
        Message msg = messageRepo.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        msg.update(request.content());
        return MessageDto.from(msg);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepo.deleteById(messageId);
    }

    @Override
    public MessageDto find(UUID id) {
        return messageRepo.findById(id)
                .map(MessageDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, int page, int size) {
        var paging = PageRequest.of(page, size);
        var result = messageRepo.findAllByChannelIdOrderByCreatedAtDesc(channelId, paging);

        return PageResponseMapper.fromPage(result, MessageDto::from);
    }
}
