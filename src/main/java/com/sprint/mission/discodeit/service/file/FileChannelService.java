package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.repository.ChannelRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileChannelService implements ChannelService {


    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void save(Channel channel) {
        if (channel.getServerName() == null) {
            throw new IllegalArgumentException("채널 저장 실패");
        }
        channelRepository.save(channel);
        System.out.println("채널 저장 성공");
    }

    @Override
    public void remove(Channel entity) {
        channelRepository.remove(entity);
        System.out.println("채널 삭제 성공");
    }

    @Override
    public Channel findById(UUID uuid) {
        return channelRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("서버를 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, ChannelDTO channelDTO) {
        Channel findChannel = findById(id);
        if (findChannel.isPrivate() != channelDTO.isPrivate()) {
            System.out.println("[채널 공개 설정이 " + findChannel.isPrivate() + "에서 " + channelDTO.isPrivate() + "로 변경되었습니다]");
            findChannel.setPrivate(channelDTO.isPrivate());
        }
        if (channelDTO.getServerLevel() != null) {
            System.out.printf("[채널 레벨이 %s에서 %s로 변경되었습니다]\n", findChannel.getServerLevel(), channelDTO.getServerLevel());
            findChannel.setServerLevel(channelDTO.getServerLevel());
        }
        if (channelDTO.getServerName() != null) {
            System.out.printf("[채널 이름이 %s에서 %s로 변경되었습니다]\n",
                    findChannel.getServerName(), channelDTO.getServerName());
            findChannel.setServerName(channelDTO.getServerName());
        }
        save(findChannel);
    }





    @Override
    public void addMember(Channel channel, UUID uuid) {
        channel.addMember(uuid);
    }

    @Override
    public void addMessageRoom(Channel channel, UUID uuid) {
        channel.addMessageRoom(uuid);
    }

    @Override
    public List<UUID> getChannelMessageRoomsId(Channel channel) {
        return channel.getMessageRooms();
    }

//    public Invitation sendInvitation(Channel fromChannel, User toUser) {
//        Invitation invitation = new Invitation(fromChannel.getId(),toUser.getId(), InvitationType.CHANNEL_INVITATION);
//        toUser.addMyInvitation(invitation);
//        return invitation;
//    }

//    public void acceptChannelRequest(User user, Invitation invitation){
//        UUID channelId = invitation.getSenderId();
//        user.addMyChannel(channelId);
//        user.removeMyInvitation(invitation);
//        Channel channel = findById(channelId);
//        channel.addMember(user);
//    }
}
