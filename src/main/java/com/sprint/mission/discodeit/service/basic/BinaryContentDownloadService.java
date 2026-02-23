package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentDownloadService {

    private final BinaryContentStorage binaryContentStorage;

//    @Override
    public ResponseEntity<Resource> download(UUID binaryContentId) {
        //💎🌱 파일 다운로드
        return binaryContentStorage.download(binaryContentId);
    }
}
