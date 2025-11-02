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




    BinaryContent save(BinaryContent binary);

    Optional<BinaryContent> find(UUID binaryId);

    List<BinaryContent> findAll();

    void delete(UUID contentId);


  //  List<UserStatus> findAllByUpdatedAtAfter(Instant since);


}
