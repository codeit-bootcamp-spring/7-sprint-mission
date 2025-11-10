package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.BasicServerService;
import com.sprint.mission.discodeit.application.dto.request.ServerCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ServerRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ServerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/servers")
public class ServerController {

    private final BasicServerService serverService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ServerResponseDto createServer(@RequestBody ServerCreateRequestDto requestDto){
        ServerResponseDto responseDto = serverService.createServer(requestDto);
        return responseDto;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ServerResponseDto editServerInfo(@RequestBody ServerRequestDto requestDto){
        ServerResponseDto serverResponseDto = serverService.updateServer(requestDto);
        return  serverResponseDto;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String deleteServer(@RequestBody ServerRequestDto requestDto){
        serverService.deleteServer(requestDto);
        return "삭제 완료";
    }

    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public List<String> findServersByUserId(@PathVariable UUID userId){
        return serverService.findAllByUserId(userId);
    }
}
