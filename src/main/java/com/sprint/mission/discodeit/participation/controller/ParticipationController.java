package com.sprint.mission.discodeit.participation.controller;

import com.sprint.mission.discodeit.application.service.ParticipationManagementService;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.participation.dto.ParticipationRequestDTO;
import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/participation")
@RequiredArgsConstructor
public class ParticipationController {
    private final ParticipationService participationService;
    private final ParticipationManagementService participationManagementService;
    private final UserManagementService userManagementService;

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ResponseEntity<?> joinChannel(
            AuthUserDTO authUser,
            @RequestBody @Valid ParticipationRequestDTO requestDTO
            ){
            return ResponseEntity.status(HttpStatus.CREATED).body(participationService.joinChannel(requestDTO, authUser.authUserId()));
    }

    @RequestMapping(value = "/leave/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> leaveChannel(
            AuthUserDTO authUser,
            @PathVariable("channelId") UUID channelId
    ){
        participationService.leaveChannel(channelId, authUser.authUserId());

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/participationInfo",method = RequestMethod.GET)
    public ResponseEntity<?> getParticipationInfo(
            AuthUserDTO userDTO
    ){
        return ResponseEntity.ok().body(userManagementService.getSimpleChannels(userDTO.authUserId()));
    }

}
