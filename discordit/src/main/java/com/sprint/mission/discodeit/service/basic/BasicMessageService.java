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
import com.sprint.mission.discodeit.event.dto.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.dto.MessageCreatedEvent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;
    private final ApplicationEventPublisher publisher;

    @Override
    public PageResponse<Message> getAllByChannelId(MessageGetRequest request) {
        log.info("채널 전체 메세지 조회 요청 들어옴. - 채널 {} ", request.channelId());
        return pageResponseMapper.fromPage(messageRepository.findAllByChannel(
                channelRepository.findById(request.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(request.channelId())),
                PageRequest.of(
                        request.pageable().getPageNumber(),
                        request.pageable().getPageSize(),
                        request.pageable().getSort()))
        );
    }

    @Override
    @Transactional
    public void remove(UUID id) {
        log.warn("메세지 삭제 요청 들어옴 - {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        binaryContentRepository.deleteAll(message.getAttachments());
        log.info("메세지 첨부파일 삭제 완료");
        messageRepository.delete(message);
        log.warn("메세지 삭제 완료");
    }

    @Override
    @Transactional
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
    @Transactional
    public MessageDto send(MessageCreateRequest messageCreateRequest, List<MultipartFile> files) {
        log.info("메세지 생성(발신) 요청 들어옴. \n\t송신자\t : {}\n\t채널\t : {}\n\t첨부파일 {}개",
                messageCreateRequest.authorId(),
                messageCreateRequest.channelId(),
                files == null ? 0 : files.size());
        Message message = messageRepository.save(new Message(
                userRepository.findById(messageCreateRequest.authorId())
                        .orElseThrow(() -> new UserNotFoundException(messageCreateRequest.authorId())),
                channelRepository.findById(messageCreateRequest.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(messageCreateRequest.channelId())),
                messageCreateRequest.content(),
                files == null ? null : files.stream()
                        .map(f -> {
                            BinaryContent attachment = new BinaryContent(f.getOriginalFilename(), f.getSize(), f.getContentType());
                            try {
                                publisher.publishEvent(new BinaryContentCreatedEvent(attachment, f.getBytes()));
                            } catch (IOException e) {
                                log.error("파일 저장 중 예상치 못한 오류 발생 : {}", attachment.getId());
                                attachment.uploadFailed();
                            }
                            return attachment;
                        }).toList()
        ));
        log.info("메세지 발신 완료.");
        publisher.publishEvent(new MessageCreatedEvent(message.getCreatedAt(), message.getChannel().getId(), message.getAuthor().getId(), message.getContent()));
        return messageMapper.toDto(message);
    }
}
