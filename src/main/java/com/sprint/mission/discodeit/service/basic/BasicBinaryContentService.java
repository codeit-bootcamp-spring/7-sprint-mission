package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.domain.file.FileNotExistException;
import com.sprint.mission.discodeit.mapper.mapStruct.BinaryStruct;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public BinaryContentDto createBinaryContent(BinaryContentCreateRequestDto binaryContentCreateRequestDto) throws IOException {
        BinaryContent binaryContent = new BinaryContent(
                binaryContentCreateRequestDto.getFileName(),
                binaryContentCreateRequestDto.getContentType(),
                binaryContentCreateRequestDto.getSize()
        );
        binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(),binaryContentCreateRequestDto.getBytes());
        return BinaryStruct.INSTANCE.toDto(binaryContent);


    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto find(UUID binaryContentID) {

        return  BinaryContentDto.from(
                binaryContentRepository.findById(binaryContentID)
                        .orElseThrow(
                                ()->new FileNotExistException(
                                        binaryContentID))) ;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIdList) {
       return binaryContentIdList.stream().map(this::find)
               .toList();

    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
    binaryContentRepository.deleteById(binaryContentId);
    }


    @Override
    public List<BinaryContent> findAll() {
        return binaryContentRepository.findAll();
    }

    @Override
    public void resetBinaryContentService() {
        binaryContentRepository.deleteAll();
    }

    @Override
    public ResponseEntity<?> downloadFile(UUID binaryContentId)  {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow();
        BinaryContentDto binaryContentDto = BinaryStruct.INSTANCE.toDto(binaryContent);
        return binaryContentStorage.download(binaryContentDto);

    }
}
