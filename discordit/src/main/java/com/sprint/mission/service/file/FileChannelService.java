package com.sprint.mission.service.file;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.file.FileChannelRepository;
import com.sprint.mission.repository.jcf.JCFChannelRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.jcf.JCFChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private static final FileChannelRepository repository = FileChannelRepository.getInstance();
    private static final FileChannelService instance = new FileChannelService();

    private FileChannelService() {}

    public static FileChannelService getInstance(){
        return instance;
    }

    @Override
    public UUID createChannel(String name, Channel.ChannelType type, User... moderators) {
        HashSet<User> moderatorSet = new HashSet<>(Arrays.asList(moderators));
        Channel channel = new Channel(name, type, moderatorSet);
        repository.save(channel);
        return channel.getUuid();
    }

    @Override
    public List<UUID> getAllChannelIds() {
        return repository.findAll().stream()
                .map(Channel::getUuid)
                .toList();
    }

    public UUID getNthChannel(int index){
        return repository.findAll().get(index).getUuid();
    }

    @Override
    public List<UUID> getRegisteredChannels(User user) {
        List<UUID> registered = new ArrayList<>();
        for (Channel c : repository.findAll()) {
            if (c.getMembers().contains(user))
                registered.add(c.getUuid());
        }
        return registered;
    }

    @Override
    public List<UUID> getNotRegisteredChannels(User user) {
        List<UUID> registered = getRegisteredChannels(user);
        List<UUID> allChannels = repository.findAll().stream()
                .map(Channel::getUuid)
                .collect(Collectors.toCollection(ArrayList::new));
        allChannels.removeAll(registered);
        return allChannels;
    }

    @Override
    public void setChannelName(UUID uuid, String name) {
        Channel channel = repository.findById(uuid);
        channel.setChannelName(name);
        repository.update(channel);
    }

    @Override
    public Channel getChannelById(UUID uuid) {
        return repository.findById(uuid);
    }

    @Override
    public Set<User> getAllMembers(UUID uuid) {
        return repository.findById(uuid).getMembers();
    }

    @Override
    public Set<User> getAllModerators(UUID uuid) {
        return repository.findById(uuid).getModerators();
    }

    @Override
    public void addMember(UUID uuid, User user) {
        Channel channel = repository.findById(uuid);
        channel.addMember(user);
    }

    @Override
    public void addModerator(UUID uuid, User user) {
        repository.findById(uuid).addModerator(user);
    }

    @Override
    public void deleteMember(UUID uuid, User user) {
        repository.findById(uuid).deleteMember(user);
    }

    @Override
    public void deleteModerator(UUID uuid, User user) {
        repository.findById(uuid).deleteModerator(user);
    }

}
