package com.sprint.mission.discodeit.participation;

import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.enums.Role;
import com.sprint.mission.discodeit.config.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.config.exception.UserNotFoundException;
import com.sprint.mission.discodeit.participation.dto.ParticipationRequestDTO;
import com.sprint.mission.discodeit.participation.dto.ParticipationResponseDTO;
import com.sprint.mission.discodeit.user.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class ParticipationServiceImpl extends BaseServiceImpl<Participation, ParticipationDualKey, ParticipationRepository> implements ParticipationService {
    private final UserService userService;
    private final ChannelService channelService;

    public ParticipationServiceImpl(ParticipationRepository participationRepository, UserService userService, ChannelService channelService) {
        super(participationRepository);
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public ParticipationResponseDTO joinChannel(ParticipationRequestDTO requestDTO, UUID userId) {
        UUID channelId = requestDTO.channelId();
        String nickname = requestDTO.nickname();
        // 1. 사용자 및 채널의 존재 여부를 먼저 확인합니다.
        validateUserAndChannelExist(userId, channelId);

        ParticipationDualKey participationId = new ParticipationDualKey(channelId, userId);

        if(existsById(participationId)){
            Participation p = findByIdNonDel(participationId);
            if (p.isDeleted()) {
                // 논리적으로 삭제된 상태이면, 복원하고 정보를 업데이트합니다.
                p.restore(); // isDeleted를 false로 변경
                p.changeNickname(nickname); // 닉네임 변경
                return ParticipationResponseDTO.from(p);
            }
            // 이미 활성 상태이면, 예외를 발생시킵니다.
            throw new IllegalStateException("이미 채널에 참가 되어있습니다.");
        }
        // 3b. 기존 참여 정보가 전혀 없는 경우 (최초 참여)
        Participation newParticipation;
        // 새로운 참여 정보를 생성하고 저장합니다.
        if(userService.findById(userId).getUsername().equals("admin")){
            newParticipation = Participation.create(channelId, userId, nickname, Role.ADMIN);
        }else{
            newParticipation = Participation.create(channelId, userId, nickname, Role.USER);
        }
        return ParticipationResponseDTO.from(newParticipation);
    }

    @Override
    public ParticipationResponseDTO setReadAt(UUID channelId, UUID userId) {
        Participation participation = findParticipation(channelId, userId);
        participation.setLastReadAt(Instant.now());
        return ParticipationResponseDTO.from(save(participation));
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        // 1. 해당 참여 정보를 조회합니다. (없으면 예외 발생)
        Participation participation = findParticipation(channelId, userId);

        if (isLastAdmin(channelId, userId)) {
            channelService.deleteById(channelId);
        }

        softDeleteById(participation.getId());

        // 삭제 후, 채널에 활성 참여자가 남아있는지 확인하여 결과를 반환합니다.
        repository.findAllByChannelId(channelId);
    }

    @Override
    public void kickUserFromChannel(UUID channelId, UUID userIdToKick, UUID adminUserId) {
        // [엣지 케이스] 자기 자신을 강퇴할 수 없습니다.
        if (userIdToKick.equals(adminUserId)) {
            throw new IllegalArgumentException("자기 자신을 강퇴할 수 없습니다.");
        }

        // 1. 요청자가 채널의 소유주(ADMIN)인지 권한을 확인합니다.
        if (isOwner(channelId, adminUserId)) {
            throw new SecurityException("사용자를 강제 퇴장시킬 권한이 없습니다.");
        }

        Participation participationToKick = findParticipation(channelId, userIdToKick);
        softDeleteById(participationToKick.getId());
    }

    @Override
    public List<ParticipationResponseDTO> findParticipationsByChannelId(UUID channelId) {
        return repository.findAllByChannelId(channelId).stream()
                .map(ParticipationResponseDTO::from)
                .toList();
    }

    @Override
    public List<ParticipationResponseDTO> findParticipationsByUserId(UUID userId) {
        return repository.findAllByUserId(userId).stream()
                .map(ParticipationResponseDTO::from)
                .toList();
    }

    @Override
    public void changeRole(UUID channelId, UUID targetUserId, UUID actorId, Role newRole) {
        // 1. 요청자(actor)가 채널의 소유주(ADMIN)인지 권한을 확인합니다.
        if (isOwner(channelId, actorId)) {
            throw new SecurityException("사용자의 역할을 변경할 권한이 없습니다.");
        }

        // 3. 역할을 변경할 대상 사용자의 참여 정보를 조회합니다.
        Participation targetParticipation = findParticipation(channelId, targetUserId);

        if (isLastAdmin(channelId, targetUserId)) {
            throw new IllegalStateException("채널의 마지막 관리자 역할은 변경할 수 없습니다.");
        }

        // 5. 모든 검증을 통과하면 역할을 변경하고 저장합니다.
        targetParticipation.changeRole(newRole);
        save(targetParticipation);
    }

    @Override
    public void changeNickname(UUID channelId, UUID userId, String newNickname) {
        Participation participation = findParticipation(channelId, userId);
        participation.changeNickname(newNickname);
        save(participation);
    }

    @Override
    public boolean isUserInChannel(UUID channelId, UUID userId) {
        ParticipationDualKey id = new ParticipationDualKey(channelId, userId);
        // existsByIdNonDel을 통해 삭제되지 않은 참여 정보가 있는지 확인합니다.
        return repository.existsByIdNonDel(id);
    }

    @Override
    public List<ParticipationResponseDTO> findOwner(UUID channelId) {
        return repository.findAllByChannelId(channelId).stream()
                .filter(p -> p.getRole() == Role.ADMIN).map(ParticipationResponseDTO::from)
                .toList();
    }

    @Override
    public boolean isOwner(UUID channelId, UUID userId) {
        try {
            return findParticipation(channelId, userId).getRole() == Role.ADMIN;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("채널에 참여한 사용자가 아닙니다.");
        }
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        List<Participation> participationsToDel = repository.findAllByUserId(userId);
        if(!participationsToDel.isEmpty()){
            deleteAllByIds(participationsToDel.stream()
                    .map(Participation::getId)
                    .toList());
        }
    }

    // --- Private Helper Methods ---

    /**
     * 특정 채널과 사용자에 해당하는 (삭제되지 않은) 참여 정보를 조회합니다.
     * @return Participation 객체
     * @throws NoSuchElementException 참여 정보가 없거나 삭제된 경우
     */
    private Participation findParticipation(UUID channelId, UUID userId) {
        ParticipationDualKey id = new ParticipationDualKey(channelId, userId);
        return findByIdNonDel(id);
    }

    /**
     * 사용자와 채널이 실제로 존재하는지(삭제되지 않았는지) 검증합니다.
     * @throws NoSuchElementException 사용자나 채널이 없거나 삭제된 경우
     */
    private void validateUserAndChannelExist(UUID userId, UUID channelId) {
        if (!userService.existsByIdNonDel(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!channelService.existsByIdNonDel(channelId)) {
            throw new ChannelNotFoundException(channelId);
        }
    }

    /**
     * 해당 사용자가 채널의 마지막 관리자인지 확인합니다.
     */
    private boolean isLastAdmin(UUID channelId, UUID userId) {
        // 현재 사용자가 관리자가 아니면, 마지막 관리자일 수 없습니다.
        if (isOwner(channelId, userId)) {
            return false;
        }
        // 채널의 모든 관리자 수를 셉니다.
        long adminCount = repository.findAllByChannelId(channelId).stream()
                .filter(p -> p.getRole() == Role.ADMIN)
                .count();
        // 관리자 수가 1명이면, 현재 사용자가 마지막 관리자입니다.
        return adminCount == 1;
    }
}