package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryRepository binaryRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public BinaryContent create(BinaryContentCreateRequest request) {
        //  String fileName = request.fileName();
        //   byte[] bytes = request.bytes();
        //     String contentType = request.contentType();
        BinaryContent binaryContent = new BinaryContent(
                request.fileName(),
                (long) request.bytes().length,
                request.contentType()
        );
        BinaryContent saved = binaryRepository.save(binaryContent);
        //따로 바이너리 저장 로직
        binaryContentStorage.put(saved.getId(), request.bytes());
        return saved;

    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentDto find(UUID BinaryContentId) {
        return binaryRepository.findById(BinaryContentId)
                .map(binaryContentMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + BinaryContentId));

    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryRepository.findAllByIdIn(binaryContentIds)
                .stream()
                .map(binaryContentMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void delete(UUID BinaryContentId) {

        binaryRepository.deleteById(BinaryContentId);
    }
}
