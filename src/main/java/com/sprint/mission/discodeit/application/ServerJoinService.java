package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerJoinService {

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;

    public void addMember(UUID userId, UUID serverId){
        Server server = ServerFindHelper.findById(serverRepository, serverId);
        User user = UserFindHelper.findById(userRepository, userId);
        server.addMember(user);
    }

    public void addMembers(List<UUID> usersId, UUID serverId){
        Server server = ServerFindHelper.findById(serverRepository, serverId);
        List<User> list = usersId.stream().map(id -> UserFindHelper.findById(userRepository, id)).toList();
        list.forEach(user -> server.addMember(user));
    }
}
