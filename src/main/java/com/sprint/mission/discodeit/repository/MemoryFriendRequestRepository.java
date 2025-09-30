package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.FriendRequest;

import java.util.*;
import java.util.stream.Stream;

public class MemoryFriendRequestRepository {
    //키는 요청을 받은 사람의 id
    private final Map<UUID, List<FriendRequest>> store =new HashMap<>();

    public void save(FriendRequest friendRequest){
        UUID key = friendRequest.getReceiverId();
        store.computeIfAbsent(key, k -> new ArrayList<>())
                .add(friendRequest);
    }
    public List<FriendRequest> findById(UUID id){
        return store.get(id);
    }

    public void remove(UUID id, FriendRequest request){
        store.get(id).remove(request);
    }
}
