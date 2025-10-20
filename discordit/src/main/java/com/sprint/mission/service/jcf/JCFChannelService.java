package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.jcf.JCFChannelRepository;
import com.sprint.mission.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    @Override
    public UUID createChannel(String name, Channel.ChannelType type, User... moderators) {
        HashSet<User> moderatorSet = new HashSet<>(Arrays.asList(moderators));
        Channel channel = new Channel(name, type, moderatorSet);
        channelRepository.save(channel);
        return channel.getUuid();
    }

    @Override
    public List<UUID> getAllChannelIds() {
        return channelRepository.findAll().stream()
                .map(Channel::getUuid)
                .toList();
    }

    public UUID getNthChannel(int index){
        return channelRepository.findAll().get(index).getUuid();
    }

    @Override
    public List<UUID> getRegisteredChannels(User user) {
        List<UUID> registered = new ArrayList<>();
        for (Channel c : channelRepository.findAll()) {
            if (c.getMembers().contains(user))
                registered.add(c.getUuid());
        }
        return registered;
    }

    @Override
    public List<UUID> getNotRegisteredChannels(User user) {
        List<UUID> registered = getRegisteredChannels(user);
        List<UUID> allChannels = channelRepository.findAll().stream()
                .map(Channel::getUuid)
                .collect(Collectors.toCollection(ArrayList::new));
        allChannels.removeAll(registered);
        return allChannels;
    }

    @Override
    public void setChannelName(UUID uuid, String name) {
        Channel channel = channelRepository.findById(uuid);
        channel.setChannelName(name);
        channelRepository.update(channel);
    }

    @Override
    public Channel getChannelById(UUID uuid) {
        return channelRepository.findById(uuid);
    }

    @Override
    public Set<User> getAllMembers(UUID uuid) {
        return channelRepository.findById(uuid).getMembers();
    }

    @Override
    public Set<User> getAllModerators(UUID uuid) {
        return channelRepository.findById(uuid).getModerators();
    }

    @Override
    public void addMember(UUID uuid, User user) {
        Channel channel = channelRepository.findById(uuid);
        channel.addMember(user);
    }

    @Override
    public void addModerator(UUID uuid, User user) {
        channelRepository.findById(uuid).addModerator(user);
    }

    @Override
    public void deleteMember(UUID uuid, User user) {
        channelRepository.findById(uuid).deleteMember(user);
    }

    @Override
    public void deleteModerator(UUID uuid, User user) {
        channelRepository.findById(uuid).deleteModerator(user);
    }

}
