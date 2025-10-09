package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ParticipationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ParticipationService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFParticipationService extends JCFBaseService<Participation, ParticipationDualKey, ParticipationRepository> implements ParticipationService {

    // Service 대신 Repository를 직접 참조하여 순환 참조를 방지합니다.
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public JCFParticipationService(ParticipationRepository participationRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        super(participationRepository);
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Participation joinChannel(UUID channelId, UUID userId, String nickname) {
        // 1. 사용자 및 채널의 존재 여부를 먼저 확인합니다.
        validateUserAndChannelExist(userId, channelId);

        ParticipationDualKey participationId = new ParticipationDualKey(channelId, userId);

        // 2. Repository의 findById를 직접 호출하여 Optional<Participation>을 받습니다.
        Optional<Participation> existingParticipation = participationRepository.findById(participationId);

        // 3. Optional 객체를 사용하여 분기 처리를 합니다.
        if (existingParticipation.isPresent()) {
            // 3a. 기존 참여 정보가 있는 경우 (재참여 또는 이미 참여 중)
            Participation p = existingParticipation.get();
            if (p.isDeleted()) {
                // 논리적으로 삭제된 상태이면, 복원하고 정보를 업데이트합니다.
                p.restore(); // isDeleted를 false로 변경
                p.changeNickname(nickname); // 닉네임 변경
                participationRepository.save(p);
                return p;
            } else {
                // 이미 활성 상태이면, 예외를 발생시킵니다.
                throw new IllegalStateException("이미 채널에 참가 되어있습니다.");
            }
        } else {
            // 3b. 기존 참여 정보가 전혀 없는 경우 (최초 참여)
            // 새로운 참여 정보를 생성하고 저장합니다.
            Participation newParticipation = Participation.create(channelId, userId, nickname, Role.USER);
            participationRepository.save(newParticipation);
            return newParticipation;
        }
    }

    @Override
    public boolean leaveChannel(UUID channelId, UUID userId) {
        // 1. 해당 참여 정보를 조회합니다. (없으면 예외 발생)
        Participation participation = findParticipation(channelId, userId);

        // [엣지 케이스] 채널의 마지막 관리자인 경우, 채널을 떠날 수 없습니다.
        if (isLastAdmin(channelId, userId)) {
            throw new IllegalStateException("채널의 마지막 관리자는 채널을 떠날 수 없습니다.");
        }

        softDeleteById(participation.getId());

        // 삭제 후, 채널에 활성 참여자가 남아있는지 확인하여 결과를 반환합니다.
        return participationRepository.findAllByChannelId(channelId).isEmpty();
    }

    @Override
    public void kickUserFromChannel(UUID channelId, UUID userIdToKick, UUID adminUserId) {
        // [엣지 케이스] 자기 자신을 강퇴할 수 없습니다.
        if (userIdToKick.equals(adminUserId)) {
            throw new IllegalArgumentException("자기 자신을 강퇴할 수 없습니다.");
        }

        // 1. 요청자가 채널의 소유주(ADMIN)인지 권한을 확인합니다.
        if (!isOwner(channelId, adminUserId)) {
            throw new SecurityException("사용자를 강제 퇴장시킬 권한이 없습니다.");
        }

        Participation participationToKick = findParticipation(channelId, userIdToKick);
        softDeleteById(participationToKick.getId());
    }

    @Override
    public List<Participation> findParticipationsByChannelId(UUID channelId) {
        return participationRepository.findAllByChannelId(channelId);
    }

    @Override
    public List<Participation> findParticipationsByUserId(UUID userId) {
        return participationRepository.findAllByUserId(userId);
    }

    @Override
    public void changeRole(UUID channelId, UUID targetUserId, UUID actorId, Role newRole) {
        // 1. 요청자(actor)가 채널의 소유주(ADMIN)인지 권한을 확인합니다.
        if (!isOwner(channelId, actorId)) {
            throw new SecurityException("사용자의 역할을 변경할 권한이 없습니다.");
        }

        // 3. 역할을 변경할 대상 사용자의 참여 정보를 조회합니다.
        Participation targetParticipation = findParticipation(channelId, targetUserId);

        if (isLastAdmin(channelId, targetUserId)) {
            throw new IllegalStateException("채널의 마지막 관리자 역할은 변경할 수 없습니다.");
        }

        // 5. 모든 검증을 통과하면 역할을 변경하고 저장합니다.
        targetParticipation.changeRole(newRole);
        participationRepository.save(targetParticipation);
    }

    @Override
    public void changeNickname(UUID channelId, UUID userId, String newNickname) {
        Participation participation = findParticipation(channelId, userId);
        participation.changeNickname(newNickname);
        participationRepository.save(participation);
    }

    @Override
    public boolean isUserInChannel(UUID channelId, UUID userId) {
        ParticipationDualKey id = new ParticipationDualKey(channelId, userId);
        // existsByIdNonDel을 통해 삭제되지 않은 참여 정보가 있는지 확인합니다.
        return participationRepository.existsByIdNonDel(id);
    }

    @Override
    public User findOwner(UUID channelId) {
        return participationRepository.findAllByChannelId(channelId).stream()
                .filter(p -> p.getRole() == Role.ADMIN) // ADMIN을 소유주로 가정
                .findFirst()
                .flatMap(p -> userRepository.findByIdNonDel(p.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("해당 채널의 소유주를 찾을 수 없습니다."));
    }

    @Override
    public boolean isOwner(UUID channelId, UUID userId) {
        try {
            return findParticipation(channelId, userId).getRole() == Role.ADMIN;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("채널에 참여한 사용자가 아닙니다.");
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
        if (!userRepository.existsByIdNonDel(userId)) {
            throw new NoSuchElementException("존재하지 않거나 탈퇴한 사용자입니다.");
        }
        if (!channelRepository.existsByIdNonDel(channelId)) {
            throw new NoSuchElementException("존재하지 않거나 삭제된 채널입니다.");
        }
    }

    /**
     * 해당 사용자가 채널의 마지막 관리자인지 확인합니다.
     */
    private boolean isLastAdmin(UUID channelId, UUID userId) {
        // 현재 사용자가 관리자가 아니면, 마지막 관리자일 수 없습니다.
        if (!isOwner(channelId, userId)) {
            return false;
        }
        // 채널의 모든 관리자 수를 셉니다.
        long adminCount = participationRepository.findAllByChannelId(channelId).stream()
                .filter(p -> p.getRole() == Role.ADMIN)
                .count();
        // 관리자 수가 1명이면, 현재 사용자가 마지막 관리자입니다.
        return adminCount == 1;
    }
}