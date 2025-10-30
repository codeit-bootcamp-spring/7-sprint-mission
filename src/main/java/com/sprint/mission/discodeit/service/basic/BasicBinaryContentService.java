package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto createBinaryContent(CreateBinaryContentRequestDto CreateDto) {

        BinaryContent binaryContent = new BinaryContent(
                CreateDto.data(),
                CreateDto.fileName(),
                CreateDto.fileType()
        );

        BinaryContent save = binaryContentRepository.save(binaryContent);
        return BinaryContentResponseDto.from(save);
    }

    @Override
    public BinaryContentResponseDto find(UUID BinaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(BinaryContentId)
                .orElseThrow(() -> new IllegalArgumentException("BinaryContent를 찾을 수 없습니다."));
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> BinaryContentIds) {
        List<BinaryContent> allByIdIn = binaryContentRepository.findAllByIdIn(BinaryContentIds);
        List<BinaryContentResponseDto> dtoList = new ArrayList<>();
        for(BinaryContent binaryContent : allByIdIn){
            dtoList.add(BinaryContentResponseDto.from(binaryContent));
        }
        return dtoList;
    }

    @Override
    public void delete(UUID BinaryContentId) {
        binaryContentRepository.delete(BinaryContentId);
    }
}
