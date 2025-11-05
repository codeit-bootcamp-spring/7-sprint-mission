package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.dto.ChannelSummaryDTO;
import com.sprint.mission.discodeit.application.dto.SimpleChannelDTO;
import com.sprint.mission.discodeit.application.dto.UserDetailInfoDTO;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.ContentOwner;
import com.sprint.mission.discodeit.config.enums.Status;
import com.sprint.mission.discodeit.content.binary.BinaryContentService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.participation.dto.ParticipationResponseDTO;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserService userService;
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;
    private final DirectMessageService directMessageService;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;
    private final ChannelService channelService;

    @Override
    public UserResponseDTO createUserWithRelatedData(UserRequestDTO requestDTO, MultipartFile multipartFile) {
        User newUser = userService.createUser(requestDTO);

        userService.save(newUser);
        userStatusService.create(newUser.getId());
        if (multipartFile != null && !multipartFile.isEmpty()) {
            ContentOwner owner = ContentOwner.USER;
            try {
                binaryContentService.uploadFile(newUser.getId(), owner, multipartFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return UserResponseDTO.fromEntity(newUser);
    }

    @Override
    public void deleteUserWithRelatedData(UUID userId) {
        participationService.deleteAllByUserId(userId);

        channelMessageService.deleteAllBySenderId(userId);

        directMessageService.delAllBySenderId(userId);

        userStatusService.deleteByUserId(userId);

        binaryContentService.deleteAllByOwnerId(userId);

        userService.deleteById(userId);
    }

    @Override
    public UserDetailInfoDTO getUserDetailInfo(UUID userId) {
        UserResponseDTO user = UserResponseDTO.fromEntity(userService.findById(userId));
        Status currentStatus = userStatusService.findByUserId(userId).currentStatus();
        int unreadDirectMessageCount = directMessageService.getUnreadDirectMessageCount(userId);
        String userProfileImagePath = binaryContentService.findAllByOwnerId(userId).get(1).filePath();

        List<ChannelResponseDTO> channels = participationService.findParticipationsByUserId(userId).stream()
                .map(participaton -> {
                    UUID channelId = participaton.participationDualKey().channelId();

                    Channel channel = channelService.findById(channelId);

                    if(channel == null) {
                        return ChannelResponseDTO.from(channel);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        List<ChannelSummaryDTO> channelSummaryList = channels.stream()
                .map(channelDTO -> {
                    int unreadcount = channelMessageService.countNotReadChannelMessage(channelDTO.id(),userId);

                    return ChannelSummaryDTO.from(
                            channelDTO,unreadcount
                    );
                })
                .toList();



        return new UserDetailInfoDTO(
                user,
                channelSummaryList,
                currentStatus,
                unreadDirectMessageCount,
                userProfileImagePath
        );
    }

    @Override
    public List<SimpleChannelDTO> getSimpleChannels(UUID userId) {
        List<ParticipationResponseDTO> participations = participationService.findParticipationsByUserId(userId);
        List<UUID> channelIds = participations.stream()
                .map(p -> p.participationDualKey().channelId())
                .toList();


        return channelService.findAllByIdNonDel(channelIds).stream()
                .map(SimpleChannelDTO::from)
                .toList();
    }
}
