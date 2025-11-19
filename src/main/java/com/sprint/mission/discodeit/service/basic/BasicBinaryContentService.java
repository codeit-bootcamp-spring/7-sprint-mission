package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private  final BinaryRepository binaryRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                (long) bytes.length,
                contentType,
                bytes
        );
        return binaryRepository.save(binaryContent);

    }

    @Override
    public BinaryContent find(UUID BinaryContentId) {
        return binaryRepository.find(BinaryContentId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + BinaryContentId));

    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryRepository.findAllByIdIn(binaryContentIds).stream().toList();
    }

    @Override
    public void delete(UUID BinaryContentId) {

        binaryRepository.delete(BinaryContentId);
    }
}
