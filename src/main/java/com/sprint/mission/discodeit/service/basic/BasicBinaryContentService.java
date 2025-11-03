package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        if(binaryContentCreateRequestDto.contentType() == null) {
            throw new IllegalArgumentException("contentType must not be null");
        }
        BinaryContent bc = new BinaryContent(
                Objects.requireNonNull(binaryContentCreateRequestDto.fileName()),
                Objects.requireNonNull(binaryContentCreateRequestDto.contentType()),
                Objects.requireNonNull(binaryContentCreateRequestDto.data())
        );

        BinaryContent save = binaryContentRepository.save(bc);

        return new BinaryContentResponseDto(
                save.getId(),
                save.getCreatedAt(),
                save.getFileName(),
                save.getContentType(),
                save.getData()
        );
    }

    @Override
    public Optional<BinaryContentResponseDto> findById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return binaryContentRepository.findById(id).map( bc -> new BinaryContentResponseDto(
                bc.getId(),
                bc.getCreatedAt(),
                bc.getFileName(),
                bc.getContentType(),
                bc.getData()
        ));
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> id) {
        List<BinaryContent> bcList = binaryContentRepository.findAllByIdIn(Objects.requireNonNull(id));
        List<BinaryContentResponseDto> dtos = new ArrayList<>();
        for (BinaryContent bc : bcList) {
            dtos.add(new BinaryContentResponseDto(
                    bc.getId(),
                    bc.getCreatedAt(),
                    bc.getFileName(),
                    bc.getContentType(),
                    bc.getData()
            ));
        }
        return dtos;
    }

    @Override
    public boolean delete(UUID id) {
        return binaryContentRepository.deleteById(Objects.requireNonNull(id));
    }
}
