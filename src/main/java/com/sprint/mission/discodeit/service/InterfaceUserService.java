package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_User;
import com.sprint.mission.discodeit.entity.dto.Res_UserWithIsOnline;
import com.sprint.mission.discodeit.entity.dto.Res_User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceUserService  extends BaseInterfaceService {
    Res_User create(Dto_User userDt, Optional<Dto_BinaryContent> content);
    Res_UserWithIsOnline find(UUID userID);      // 읽기
    List<Res_UserWithIsOnline> findAll();             // 모두 읽기
    Res_User update(UUID userId, Dto_User dto_user, Optional<Dto_BinaryContent> requestDto_Content); // 수정
    void delete(UUID userID);   // 삭제
}
