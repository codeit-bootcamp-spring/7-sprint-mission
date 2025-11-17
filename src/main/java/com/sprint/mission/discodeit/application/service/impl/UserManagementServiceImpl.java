package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.dto.ChannelSummary;
import com.sprint.mission.discodeit.application.dto.ProfileUpdateResponse;
import com.sprint.mission.discodeit.application.dto.SimpleChannel;
import com.sprint.mission.discodeit.application.dto.UserDetailInfo;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.ContentOwner;
import com.sprint.mission.discodeit.config.enums.Status;
import com.sprint.mission.discodeit.content.binary.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.content.binary.BinaryContentService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.participation.dto.ParticipationResponseDTO;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
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
  public UserResponseDTO createUserWithRelatedData(UserRequestDTO requestDTO,
      MultipartFile multipartFile) {
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
  public UserDetailInfo getUserDetailInfo(UUID userId) {

    UserResponseDTO user = UserResponseDTO.fromEntity(userService.findById(userId));
    Status currentStatus = userStatusService.findByUserId(userId).currentStatus();
    int unreadDirectMessageCount = directMessageService.getUnreadDirectMessageCount(userId);

    List<String> filePaths = binaryContentService.findAllByOwnerId(userId).stream()
        .map(BinaryContentResponse::filePath)
        .toList();

    List<ChannelResponseDTO> channels = participationService.findParticipationsByUserId(userId)
        .stream()
        .map(participaton -> {
          try {
            UUID channelId = participaton.participationDualKey().channelId();
            Channel channel = channelService.findByIdNonDel(channelId);
            return ChannelResponseDTO.from(channel);
          } catch (NoSuchElementException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .toList();

//    List<ChannelSummary> channelSummaryList = channels.stream()
//        .map(channelDTO -> {
//          int unreadcount = channelMessageService.countNotReadChannelMessage(channelDTO.id(),
//              userId);
//
//          return ChannelSummary.from(
//              channelDTO, unreadcount
//          );
//        })
//        .toList();

    List<ChannelSummary> channelSummaryList = channels.stream().map(
        channel -> ChannelSummary.from(channel,
            channelMessageService.countNotReadChannelMessage(channel.id(), userId))).toList();

    return new UserDetailInfo(
        user,
        channelSummaryList,
        currentStatus,
        unreadDirectMessageCount,
        filePaths
    );
  }

  @Override
  public List<SimpleChannel> getSimpleChannels(UUID userId) {
    List<ParticipationResponseDTO> participations = participationService.findParticipationsByUserId(
        userId);
    List<UUID> channelIds = participations.stream()
        .map(p -> p.participationDualKey().channelId())
        .toList();

    return channelService.findAllByIdNonDel(channelIds).stream()
        .map(SimpleChannel::from)
        .toList();
  }

  @Override
  public ProfileUpdateResponse updateProfile(UUID userId, UserUpdateRequest request,
      MultipartFile multipartFile) {
    try {
      binaryContentService.deleteAllByOwnerId(userId);
      BinaryContentResponse binaryContentResponse = binaryContentService.uploadFile(userId,
          ContentOwner.USER, multipartFile);
      UserResponseDTO responseDTO = userService.tempUpdateProfile(userId, request);
      return ProfileUpdateResponse.from(responseDTO, binaryContentResponse);

    } catch (IOException e) {
      throw new RuntimeException("프로필 이미지 저장에 실패했습니다.");
    }
  }
}
