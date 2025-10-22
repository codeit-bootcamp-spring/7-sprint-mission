package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserRepository {
    User create(String userId, String password, String userName, String userNickname);
    User read(UUID userId);
    List<User> readAll();

    boolean delete(UUID userId);

    //수정 조회용 1안인데 내 기분에는 별로같다
    User updateName(UUID userId,String UserName);
    User updateNickName(UUID userId,String UserNickname);

    //내 목표 생각
    User update (UUID uuid, Consumer<User> updater);
}
