package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.converter.BinaryContentDtoConverter;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void create(CreateBinaryContentRequestDto request) {
        if (Optional.ofNullable(request).isPresent()) {
            BinaryContent file = new BinaryContent(
                    request.fileName(),
                    request.contentType(),
                    request.bytes()
            );
            binaryContentRepository.save(file);
        }
    }

    @Override
    public BinaryContentResponseDto find(UUID binaryContentId) {
        BinaryContent content = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUND));

        return toDto(content);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAll().stream().
                filter(content -> binaryContentIds.contains(content.getId()))
                .map(content -> toDto(content))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID binaryContentId) {
        binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUND));
        binaryContentRepository.delete(binaryContentId);
    }

    private BinaryContentResponseDto toDto(BinaryContent binaryContent) {
        return BinaryContentDtoConverter.toResponseDto(binaryContent);
    }
}
