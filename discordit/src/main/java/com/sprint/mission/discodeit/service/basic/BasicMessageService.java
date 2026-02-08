package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.message.MessageNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.channel.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.entity.message.MessageDto;
import com.sprint.mission.discodeit.dto.entity.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.entity.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.mapper.MessageMapper;
import com.sprint.mission.discodeit.dto.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;

    @Override
    public PageResponse<Message> getAllByChannelId(MessageGetRequest request) {
        log.info("채널 전체 메세지 조회 요청 들어옴. - 채널 {} ", request.channelId());
        return PageResponseMapper.fromPage(messageRepository.findAllByChannel(
                channelRepository.findById(request.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(request.channelId())),
                PageRequest.of(
                        request.pageable().getPageNumber(),
                        request.pageable().getPageSize(),
                        request.pageable().getSort()))
        );
    }

    @Override
    public void remove(UUID id) {
        log.warn("메세지 삭제 요청 들어옴 - {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        binaryContentRepository.deleteAll(message.getAttachments());
        messageRepository.delete(message);
        log.warn("메세지 삭제 완료");
    }

    @Override
    public MessageDto editMessage(UUID id, MessageEditRequest request) {
        log.info("메세지 수정 요청 들어옴 - {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        message.setContent(request.newContent());
        Message savedMessage = messageRepository.save(message);
        log.info("메세지 수정 완료.");
        return messageMapper.toDto(savedMessage);
    }

    @Override
    public List<MessageDto> getAll() {
        log.info("모든 메세지 조회 요청 들어옴.");
        return messageRepository.findAll()
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    public MessageDto send(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachmentFiles) {
        log.info("메세지 생성(발신) 요청 들어옴. \n\t송신자\t : {}\n\t채널\t : {}\n\t첨부파일 {}개",
                messageCreateRequest.authorId(),
                messageCreateRequest.channelId(),
                attachmentFiles == null? 0 : attachmentFiles.size());
        Message message = messageRepository.save(new Message(
                userRepository.findById(messageCreateRequest.authorId())
                        .orElseThrow(() -> new UserNotFoundException(messageCreateRequest.authorId())),
                channelRepository.findById(messageCreateRequest.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(messageCreateRequest.channelId())),
                messageCreateRequest.content(),
                attachmentFiles == null ? null : attachmentFiles.stream()
                        .map(f -> {
                            try {
                                BinaryContent saved = binaryContentRepository.save(new BinaryContent(f.getOriginalFilename(), f.getSize(), f.getContentType()));
                                binaryContentStorage.put(saved.getId(), f.getBytes());
                                return saved;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).toList()
        ));
        log.info("메세지 발신 완료.");
        return messageMapper.toDto(message);
    }
}
