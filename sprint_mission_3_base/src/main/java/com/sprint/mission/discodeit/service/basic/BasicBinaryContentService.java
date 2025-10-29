package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryOwnerType;
import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    // 생성
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        BinaryOwnerType ownerType = BinaryOwnerType.MESSAGE_ATTACHMENT; // 필요 시 동적으로 변경 가능

        BinaryContent bin = new BinaryContent(
                ownerType,              // BinaryOwnerType
                request.messageId(),    // UUID ownerId
                request.filename(),     // String filename
                request.contentType(),  // String contentType
                request.data()          // byte[] data
        );

        binaryContentRepository.save(bin);
        return toDto(bin);
    }

    // 단건 조회
    @Override
    public BinaryContentDto find(UUID id) {
        BinaryContent bin = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent not found: " + id));
        return toDto(bin);
    }

    // 여러 개 조회
    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids)
                .stream().map(this::toDto).toList();
    }

    // 삭제
    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException("BinaryContent not found: " + id);
        }
        binaryContentRepository.deleteById(id);
    }

    // 변환
    private BinaryContentDto toDto(BinaryContent b) {
        return new BinaryContentDto(
                b.getId(),
                b.getFilename(),
                b.getContentType(),
                b.getData(),
                b.getOwnerId(),
                b.getOwnerType()
        );
    }
}
