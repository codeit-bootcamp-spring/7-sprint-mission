package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ParticipationRepository;
import com.sprint.mission.discodeit.service.ParticipationService;

import java.util.List;
import java.util.UUID;

public class JCFParticipationService extends JCFBaseService<Participation, UUID, ParticipationRepository> implements ParticipationService {
    private final ParticipationRepository repository;
    protected JCFParticipationService(ParticipationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Participation joinChannel(UUID channelId, UUID userId, String nickname) {
        if(channelId == null){
            throw new IllegalArgumentException("해당 채널을 찾을 수 없습니다.");
        }
        if(userId == null){
            throw new IllegalArgumentException("해당 사용자을 찾을 수 없습니다.");
        }
        if(isChannelUserExist(channelId, userId)){
            throw new IllegalArgumentException("이미 채널에 참가 되어있습니다.");
        }
        Participation participation = Participation.create(channelId, userId, nickname, Role.USER);
        repository.save(participation);
        return participation;
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        if(channelId == null){
            throw new IllegalArgumentException("해당 채널을 찾을 수 없습니다.");
        }
        if(userId == null){
            throw new IllegalArgumentException("해당 사용자을 찾을 수 없습니다.");
        }
        if(!isChannelUserExist(channelId, userId)){
            throw new IllegalArgumentException("채널에 참여 되어 있지 않습니다.");
        }
    }

    @Override
    public void kickUserFromChannel(UUID channelId, UUID userIdToKick, UUID adminUserId) {
        if()
    }

    @Override
    public List<Participation> findParticipationsByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public User findOwner(UUID channelId) {
        return null;
    }
    @Override
    public boolean isChannelOwner(UUID uuid) {
        return false;
    }

    @Override
    public List<Participation> findParticipationsByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public boolean isUserInChannel(UUID channelId, UUID userId) {
        return false;
    }

    @Override
    public void changeRole(UUID channelId, UUID userId, Role newRole) {

    }

    @Override
    public void changeNickname(UUID channelId, UUID userId, String newNickname) {

    }

    @Override
    public boolean isChannelUserExist(UUID channelId, UUID userId) {
        return false;
    }



}
