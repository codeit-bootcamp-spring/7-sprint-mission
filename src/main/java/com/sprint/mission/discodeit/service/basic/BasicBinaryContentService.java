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
    public ResponseEntity<BinaryContentResponse> create(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(request.contentsType(), request.contentByte(),"null");
        binaryRepository.save(binaryContent);

        return  ResponseEntity.ok(BinaryContentResponse.from(binaryContent));

    }

    @Override
    public BinaryContentResponse find(UUID BinaryContentId) {
        BinaryContent binaryContent = binaryRepository.find(BinaryContentId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + BinaryContentId));
        return BinaryContentResponse.from(binaryContent);
    }

    @Override
    public List<BinaryContentResponse> findAllByIn(UUID binaryContentId) {
        return binaryRepository.findAll().stream()
                .filter(binaryContent -> binaryContent.getId().equals(binaryContentId))
                .map(BinaryContentResponse::from)
                .toList();
    }

    @Override
    public void delete(UUID BinaryContentId) {
        binaryRepository.delete(BinaryContentId);
    }
}
