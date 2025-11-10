package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.channel.dto.ChannelRequestDTO;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.Role;
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
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ParticipationService participationService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDTO> createChannel(
            @SessionAttribute() AuthUserDTO authUser,
            @RequestBody @Valid ChannelRequestDTO requestDTO) {

        ChannelResponseDTO channelDTO = channelService.create(requestDTO);

        ParticipationRequestDTO participationRequest = new ParticipationRequestDTO(
                channelDTO.id(),
                authUser.username(),
                Role.ADMIN
        );
        participationService.joinChannel(participationRequest, authUser.authUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(channelDTO);
    }
    @RequestMapping(value = "/changeSettings/{channelId}", method = RequestMethod.POST)
    public ResponseEntity<?> changeChannelSettings(
            @PathVariable UUID channelId,
            @RequestBody @Valid ChannelRequestDTO requestDTO,
            AuthUserDTO authUser) {
        if (!participationService.isOwner(channelId, authUser.authUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("채널 관리자만 가능합니다.");
        }
        channelService.changeSettings(channelId, requestDTO);

        ChannelResponseDTO responseDTO = ChannelResponseDTO.from(channelService.findById(channelId));

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}
