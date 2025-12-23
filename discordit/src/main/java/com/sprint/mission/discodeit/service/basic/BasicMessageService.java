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
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public PageResponse<Message> getAllByChannelId(MessageGetRequest request) {
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
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        binaryContentRepository.deleteAll(message.getAttachments());
        messageRepository.delete(message);
    }

    @Override
    public MessageDto editMessage(UUID id, MessageEditRequest request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        message.setContent(request.newContent());
        Message savedMessage = messageRepository.save(message);
        return MessageMapper.toDto(savedMessage);
    }

    @Override
    public List<MessageDto> getAll() {
        return messageRepository.findAll()
                .stream()
                .map(MessageMapper::toDto)
                .toList();
    }

    @Override
    public MessageDto send(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachmentFiles) {
        Message message = messageRepository.save(new Message(
                userRepository.findById(messageCreateRequest.authorId())
                        .orElseThrow(() -> new UserNotFoundException(messageCreateRequest.authorId())),
                channelRepository.findById(messageCreateRequest.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(messageCreateRequest.channelId())),
                messageCreateRequest.content(),
                attachmentFiles == null ? null : attachmentFiles.stream()
                        .map(f -> {
                            try {
                                BinaryContent saved = binaryContentRepository.save(new BinaryContent(f.getName(), f.getSize(), f.getContentType()));
                                binaryContentStorage.put(saved.getId(), f.getBytes());
                                return saved;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).toList()
        ));
        return MessageMapper.toDto(message);
    }
}
