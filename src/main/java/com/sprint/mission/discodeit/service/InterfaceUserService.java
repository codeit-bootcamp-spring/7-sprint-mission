package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_UserCreate;
import com.sprint.mission.discodeit.entity.dto.Dto_UserUpdate;
import com.sprint.mission.discodeit.entity.dto.UserDto;
import com.sprint.mission.discodeit.entity.dto.Res_User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceUserService  extends BaseInterfaceService {
    Res_User create(Dto_UserCreate userDt, Optional<Dto_BinaryContent> content);
    UserDto find(UUID userID);      // 읽기
    List<UserDto> findAll();             // 모두 읽기
    Res_User update(UUID userId, Dto_UserUpdate dto_userUpdate, Optional<Dto_BinaryContent> requestDto_Content); // 수정
    void delete(UUID userID);   // 삭제
}
