package com.sprint.mission.discodeit.server.application;


import com.sprint.mission.discodeit.server.domain.Server;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileServerService implements ServerService {


    private final ServerRepository serverRepository;

    public FileServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public void save(Server server) {
        serverRepository.save(server);
        System.out.println("채널 저장 성공");
    }

    @Override
    public void remove(Server entity) {
        serverRepository.remove(entity);
        System.out.println("채널 삭제 성공");
    }

    @Override
    public Server findById(UUID uuid) {
        return serverRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다."));
    }

    @Override
    public List<Server> findAll() {
        return serverRepository.findAll();
    }

}
