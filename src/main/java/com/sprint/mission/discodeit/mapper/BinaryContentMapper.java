package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BinaryContentMapper {
    public BinaryContentResponseDto toDto(BinaryContent binaryContent) {
        if (binaryContent == null) {
            return null;
        }

//        String encodedString = Base64.getEncoder().encodeToString(binaryContent.getBytes());

        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize()
        );
    }

    public List<BinaryContentResponseDto> toDtoList(List<BinaryContent> binaryContentList) {
        if(binaryContentList == null || binaryContentList.isEmpty()) {
            return List.of();
        }
        return binaryContentList.stream()
                .map(bc -> toDto(bc))
                .toList();
    }
}
