package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    @Override
    public ReadStatus create(ReadStatus readStatus) {
        return null;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return null;
    }

    @Override
    public ReadStatus delete(UUID id) {
        return null;
    }
}
