package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


public final class ServerFindHelper {
        
    
    public static Server findById(ServerRepository repo, UUID id) {
        Server server = repo.findById(id).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다"));
        return server;
    }

    public static List<UUID> findAllByUserId(ServerRepository serverRepository , UUID userId) {
        return serverRepository.findAll()
                .stream()
                .filter(server -> server.getMembers().contains(userId))
                .map(server->server.getId())
                .toList();
    }
}
