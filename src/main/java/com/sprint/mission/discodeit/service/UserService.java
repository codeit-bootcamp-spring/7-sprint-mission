package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.DTO.UserDTO;

import java.util.UUID;

public interface UserService extends BaseService<User> {
    void update(UUID id, UserDTO userDTO);


}
