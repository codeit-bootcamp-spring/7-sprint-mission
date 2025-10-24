package com.sprint.mission.discodeit.server.application;


import com.sprint.mission.discodeit.readstatus.application.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.server.domain.Server;
import com.sprint.mission.discodeit.server.presentation.ServerService;
import com.sprint.mission.discodeit.server.presentation.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.server.presentation.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.server.presentation.dto.response.ServerReadStatusDto;
import com.sprint.mission.discodeit.server.presentation.dto.response.ServerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.*;

@Service
@RequiredArgsConstructor
public class BasicServerService implements ServerService {


    private final ServerRepository serverRepository;
    private final ReadStatusRepository readStatusRepository;



    @Override
    public void createPublicServer(ServerCreateRequestDto requestDto) {
        Server server = Server.create(
                requestDto.serverName(),
                false,
                requestDto.serverLevel(),
                requestDto.members());
        serverRepository.save(server);


    }

    @Override
    public void createPrivateServer(ServerCreateRequestDto requestDto) {
        Server server = Server.create(
                requestDto.serverName(),
                true,
                requestDto.serverLevel(),
                requestDto.members());
        serverRepository.save(server);
        requestDto.members().stream()
                .map(id -> ReadStatus.create(id, server.getId()))
                .toList()
                .forEach(rs->readStatusRepository.save(rs));

    }

    @Override
    public ServerReadStatusDto findRecentReadStatus(ServerRequestDto requestDto) {
        Server server = findById(requestDto.serverId());
        ServerReadStatusDto serverReadStatusDto = serverToRecentDto(server);

        return null;
    }

    private Server findById(UUID uuid) {
        return serverRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다."));
    }

    @Override
    public List<UUID> findAllByUserId(UUID userId) {
        return serverRepository.findAll()
                .stream()
                .filter(server -> server.getMembers().contains(userId))
                .map(server->server.getId())
                .toList();

    }

    @Override
    public ServerResponseDto updateServer(ServerRequestDto requestDto) {
        Server server = findById(requestDto.serverId());
        server.updatePrivate(requestDto.isPrivate());
        if (requestDto.serverName()!=null){
            server.updateServerName(requestDto.serverName());
        }
        if(requestDto.serverLevel()!=null){
            server.updateServeLevel(requestDto.serverLevel());
        }
        serverRepository.save(server);

        return serverToResponseDto(server);
    }

    @Override
    public void deleteServer(ServerRequestDto requestDto) {
        serverRepository.remove(findById(requestDto.serverId()));
        readStatusRepository.findAll().stream()
                .filter(rs -> rs.getServerId().equals(requestDto.serverId()))
                .forEach(rs->readStatusRepository.remove(rs));
    }

    private ServerReadStatusDto serverToRecentDto(Server server){

        //가장 최근 메세지 읽은 시간와 서버 멤버 +읽은 멤버
        UUID serverId = server.getId();
        ReadStatus readStatus = readStatusRepository.findAll().stream()
                .filter(rs -> rs.getServerId().equals(serverId))
                .max(comparing(ReadStatus::getLastReadAt)).orElseThrow(()-> new NoSuchElementException());

        if (server.isPrivate()){
            return new ServerReadStatusDto(readStatus.getLastReadAt(), server.getMembers(),readStatus.getUserId());
        }

        //public일 경우 가장 최근 메세지 읽은 시간와 서버 멤버
        return new ServerReadStatusDto(readStatus.getLastReadAt(),server.getMembers(),null);

    }

    private ServerResponseDto serverToResponseDto(Server server){
        return new ServerResponseDto(server.getServerName(),
                server.getServerLevel(),
                server.isPrivate());
    }



}
