package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService{

    void save(User user);

    void remove(User user);

    User findById(UUID id);

    List<User> findAll();

    void update(UUID id, UserDto userDTO);

    User findByEmail(String email);


}
