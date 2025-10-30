package com.sprint.mission.discodeit.entity.binaryContent.service;

import com.sprint.mission.discodeit.entity.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.entity.binaryContent.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.binaryContent.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.binaryContent.dto.BinaryContentInfoDto;
import com.sprint.mission.discodeit.exception.FileSizeLimitExceededException;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    // 10MB, 500MB 업로드 제한
    private static final long BASIC_MAX_FILE_SIZE = 10 * 1024 * 1024;
//    private static final long NITRO_MAX_FILE_SIZE = 500 * 1024 * 1024;

    @Override
    public BinaryContentInfoDto createBinaryContent(BinaryContentCreateDto createDto) {
        if (createDto.data().length == 0) {
            throw new InvalidInputException("data is empty"); // 임시
        }
        // Spring은 자체적으로 1MB 제한을 건대요 그래서 500MB로 늘림, 현재는 기본으로 제한
        if (createDto.data().length > BASIC_MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("파일용량은 10MB를 넘을 수 없습니다.");
        }

        // messageId == null ? profileImage : attachment
        BinaryContent newContent = new BinaryContent(
                createDto.userId(), createDto.messageId(),
                createDto.data(), createDto.dataName(),
                createDto.dataType());
        binaryContentRepository.save(newContent);
        return BinaryContentInfoDto.from(newContent);
    }

    @Override
    public Optional<BinaryContentInfoDto> findBinaryContentByUserId(UUID id) {
        return binaryContentRepository.findById(id).map(BinaryContentInfoDto::from);
    }

    @Override
    public List<BinaryContentInfoDto> findAllBinaryContentByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(BinaryContentInfoDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContentById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
