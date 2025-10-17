package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.DeletedChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.deletedCash.DeletedChannel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {

    private final ArrayList<Channel> channelRepo ;
    private final ArrayList<DeletedChannel> deletedChannelRepo ;

    public JCFChannelRepository() {
        this.channelRepo = new ArrayList<>();
        this.deletedChannelRepo = new ArrayList<>();
        resetChannelRepository();

    }

    @Override
    public ChannelDto getChannelById(UUID channelId) {

        return channelRepo.stream().filter(x->x.getId().equals(channelId)).map(this::channelToChannelDto).findFirst().orElse(null);
    }

    @Override
    public ChannelDto getChannelByName(String channelName) {
        return channelRepo.stream().filter(x->x.getName().equals(channelName)).map(this::channelToChannelDto).findFirst().orElse(null);
    }

    @Override
    public ChannelDto getChannel(ChannelDto channelDto) {
        return getChannelById(channelDto.getId());
    }

    @Override
    public void saveChannel(ChannelDto channelDto) {

        Channel expectedChannel = channelDtoToChannel(channelDto);
        if(channelRepo.stream().anyMatch(x->x.getId().equals(expectedChannel.getId()))){
            return ;
        }
        channelRepo.add(expectedChannel);

    }

    @Override
    public void deleteChannel(ChannelDto channelDto) {
        if(channelDto == null){
            return;
        }

        channelRepo.removeIf(x->x.getId().equals(channelDto.getId()));
        deletedChannelRepo.add(channelDtoToDeletedChannel(channelDto));

    }

    @Override
    public <T> void updateChannel(ChannelDto channelDto, Channel.channelElement channelElement, T updatedContent) {

        Channel expectedChannel = channelRepo.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().orElse(null);
        BiConsumer<Channel, Object> editFunction = channelElement.setter;
        editFunction.accept(expectedChannel, updatedContent);
        expectedChannel.updateEntity();

    }

    @Override
    public ChannelDto[] getAllChannel() {
        return channelRepo.stream().map(this::channelToChannelDto).toArray(ChannelDto[]::new);
    }

    @Override
    public ChannelDto[] getUpdatedChannel() {
        return channelRepo.stream().filter(x->x.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT).map(this::channelToChannelDto).toArray(ChannelDto[]::new);
    }

    @Override
    public DeletedChannelDto[] getDeletedChannel() {
        return deletedChannelRepo.stream().map(this::deletedChannelToDeletedChannelDto).toArray(DeletedChannelDto[]::new);
    }

    @Override
    public void addUserToChannel(UserDto userDto, ChannelDto channelDto) {
        Channel targetChannel = channelRepo.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().orElse(null);
        User user = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
        targetChannel.addUserToChannel(user);

        return;


    }

    @Override
    public void deleteUserFromChannel(UserDto userDto, ChannelDto channelDto) {
        Channel targetChannel = channelRepo.stream().filter(x->x.getId().equals(channelDto.getId())).findFirst().orElse(null);
        User user = new User(userDto.getId(),userDto.getName(),userDto.getNickname(),userDto.getEmail(),userDto.isOnline());
        targetChannel.removeUserFromChannel(user);
        return;
    }

    @Override
    public void resetChannelRepository() {
        channelRepo.clear();
        deletedChannelRepo.clear();

    }

    private ChannelDto channelToChannelDto(Channel channel){
        List<UserDto> userDtoList = channel.getUserDb().stream().map(
                x->new UserDto(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).collect(Collectors.toList());

        return new ChannelDto(channel.getId(),channel.getName(),channel.getDescription(),channel.isPublic(),channel.isTextChannel(),userDtoList);
    }
    private Channel channelDtoToChannel(ChannelDto channelDto){
        List<User> userDb = channelDto.getUserDtoList().stream().map(
                x->new User(x.getId(),x.getName(),x.getNickname(),x.getEmail(),x.isOnline())
        ).collect(Collectors.toList());
        return new Channel(channelDto.getId(),channelDto.getName(),channelDto.getDescription(),channelDto.isPublic(),channelDto.isTextChannel(),userDb);
    }
    private DeletedChannelDto deletedChannelToDeletedChannelDto(DeletedChannel deletedChannel){
        return new DeletedChannelDto(deletedChannel.getName());
    }
    private DeletedChannel deletedChannelDtoToDeletedChannel(DeletedChannelDto deletedChannelDto){
        return new DeletedChannel(deletedChannelDto.getName());
    }

    private DeletedChannel channelDtoToDeletedChannel(ChannelDto channelDto){
        return new DeletedChannel(channelDto.getName());
    }
}
