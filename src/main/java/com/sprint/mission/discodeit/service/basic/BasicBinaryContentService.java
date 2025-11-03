package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.exception.FileSizeLimitExceededException;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    /* 10MB, 500MB 업로드 제한
    private static final long BASIC_MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final long NITRO_MAX_FILE_SIZE = 500 * 1024 * 1024;
     */

    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;
    private static final long MAX_FILE_SIZE_MB = 10;
    private static final long BASIC_MAX_FILE_SIZE = MAX_FILE_SIZE_MB * BYTES_PER_MB;

    @Override
    public BinaryContentResponseDto createBinaryContent(BinaryContentRequestDto requestDto) {
        if (requestDto.data().length == 0) {
            throw new InvalidInputException("data is empty"); // 임시
        }
        // Spring은 자체적으로 1MB 제한을 건대요 그래서 500MB로 늘림, 현재는 기본으로 제한
        if (requestDto.data().length > BASIC_MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("파일용량은 10MB를 넘을 수 없습니다.");
        }

        // messageId == null ? profileImage : attachment
        BinaryContent newContent = new BinaryContent(
                requestDto.userId(), requestDto.messageId(),
                requestDto.data(), requestDto.dataName(),
                requestDto.dataType());
        binaryContentRepository.save(newContent);
        return BinaryContentResponseDto.from(newContent);
    }

    @Override
    public BinaryContentResponseDto findBinaryContentByUserId(UUID id) {
        BinaryContent content=  binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));
        return BinaryContentResponseDto.from(content);
    }

    @Override
    public List<BinaryContentResponseDto> findAllBinaryContentByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(BinaryContentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContentById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
