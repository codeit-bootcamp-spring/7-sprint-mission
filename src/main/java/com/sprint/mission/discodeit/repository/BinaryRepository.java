package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.user.request.BinaryRequest;
import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface BinaryRepository {


    Optional<BinaryContent> findByUuid(UUID contentId,ContentsType contentsType);


    BinaryContent save(UUID binaryId, ContentsType contentsType, String contentID);


    void deleteByUuid(UUID contentId, ContentsType contentsType);


  //  List<UserStatus> findAllByUpdatedAtAfter(Instant since);


}
