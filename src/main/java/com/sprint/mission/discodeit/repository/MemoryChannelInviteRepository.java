package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ChannelInviteRequest;

import java.util.*;

public class MemoryChannelInviteRepository {

    private final Map<UUID, List<ChannelInviteRequest>> store=new HashMap<>();

    public void save(ChannelInviteRequest request){
        UUID key = request.getUserId();
        store.computeIfAbsent(key, k -> new ArrayList<>())
                .add(request);
    }

    public List<ChannelInviteRequest> findById(UUID uuid){
        return store.get(uuid);
    }
    public void remove(UUID userId, ChannelInviteRequest request){
        store.get(userId).remove(request);
    }
}
