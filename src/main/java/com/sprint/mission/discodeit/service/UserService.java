package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.update.UserUpdate;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(User user);               // 생성

    User read(UUID id);                   // 단건 조회

    List<User> readAll();                 // 전체 조회

    User update(UUID id, UserUpdate uu);      // 수정

    User updateState(UUID id, User.State state);

    void delete(UUID id);              // 삭제

}