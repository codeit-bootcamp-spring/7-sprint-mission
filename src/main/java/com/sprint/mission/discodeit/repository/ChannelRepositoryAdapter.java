package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpainterface.JpaChannelRepository;
import com.sprint.mission.discodeit.service.mapper.ChannelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ChannelRepositoryAdapter implements ChannelRepository {

    private final JpaChannelRepository jpa;
    private final ChannelMapper mapper;

    @Override
    public void save(Channel channel) {
        jpa.save(mapper.toChannelEntity(channel));
    }

    @Override
    public void delete(Channel channel) {
        jpa.delete(mapper.toChannelEntity(channel));
    }

    @Override
    public Optional<Channel> findById(String id) {
        return jpa.findById(id).map(mapper::toChannel);
    }

    @Override
    public List<Channel> findAll() {
        return jpa.findAll().stream().map(mapper::toChannel).toList();
    }
}
