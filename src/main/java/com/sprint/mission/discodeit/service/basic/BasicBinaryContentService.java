package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    @Override
    public BinaryContent createBinaryContent(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        BinaryContent binaryContent =BinaryContent.builder()
                .binaryFile(binaryContentCreateRequestDto.getBinaryFile())
                .binaryContentUsage(binaryContentCreateRequestDto.getBinaryContentUsage())
                .build();
        return binaryContentRepository.createBinaryContent(binaryContent);


    }

    @Override
    public BinaryContent find(UUID binaryContentID) {

        return binaryContentRepository.readBinaryContent(binaryContentID).orElseThrow(()->new IllegalArgumentException("존재하지 않는 binaryContent 입니다."));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIdList) {
       return binaryContentIdList.stream().map(this::find).toList();

    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
    binaryContentRepository.deleteBinaryContent(binaryContentId);
    }


    @Override
    public List<BinaryContent> findAll() {
        return binaryContentRepository.readAllBinaryContent();
    }
}
