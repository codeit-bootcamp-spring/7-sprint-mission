package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryRepository {


    Optional<BinaryContent> findByUuid(UUID contentId,ContentsType contentsType);

    Optional<BinaryContent> find(UUID binaryId);

    List<BinaryContent> findAll();

    BinaryContent save(BinaryContent binary);

    void deleteByUuid(UUID contentId, ContentsType contentsType);


  //  List<UserStatus> findAllByUpdatedAtAfter(Instant since);


}
