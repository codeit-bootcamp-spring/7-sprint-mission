package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryRepo;
    private final MessageRepository messageRepo;
    private final BinaryContentStorage storage;

    @Override
    public BinaryContentDto upload(BinaryContentUploadRequest req) {

        Message message = messageRepo.findById(req.messageId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        BinaryContent binary = BinaryContent.builder()
                .fileName(req.fileName())
                .size(req.bytes().length)
                .contentType(req.contentType())
                .message(message)
                .build();

        binaryRepo.save(binary);

        storage.put(binary.getId(), req.bytes());

        return BinaryContentDto.from(binary);
    }


    @Override
    public BinaryContentDto find(UUID id) {
        return binaryRepo.findById(id)
                .map(BinaryContentDto::from)
                .orElseThrow(() ->
                        new IllegalArgumentException("BinaryContent not found"));
    }

    @Override
    public void delete(UUID id) {
        binaryRepo.deleteById(id);
    }
}
