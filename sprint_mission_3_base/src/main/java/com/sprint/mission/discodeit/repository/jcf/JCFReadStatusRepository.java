package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Primary
public class JCFReadStatusRepository implements ReadStatusRepository {

    // 메모리 저장소 + 인덱스
    private final Map<UUID, ReadStatus> store = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> byChannel = new ConcurrentHashMap<>(); // channelId -> readStatusId 집합
    private final Map<UUID, Set<UUID>> byUser    = new ConcurrentHashMap<>(); // userId    -> readStatusId 집합

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        store.put(readStatus.getId(), readStatus);
        byChannel.computeIfAbsent(readStatus.getChannelId(), k -> new HashSet<>()).add(readStatus.getId());
        byUser.computeIfAbsent(readStatus.getUserId(), k -> new HashSet<>()).add(readStatus.getId());
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        Set<UUID> ids = byUser.getOrDefault(userId, Collections.emptySet());
        return ids.stream().map(store::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId) {
        // channel 인덱스 ∩ user 인덱스 교집합으로 탐색
        Set<UUID> a = byChannel.getOrDefault(channelId, Collections.emptySet());
        Set<UUID> b = byUser.getOrDefault(userId, Collections.emptySet());
        for (UUID rsId : a) {
            if (b.contains(rsId)) return Optional.ofNullable(store.get(rsId));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByChannelIdAndUserId(UUID channelId, UUID userId) {
        return findByChannelIdAndUserId(channelId, userId).isPresent();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return findByUserIdAndChannelId(userId, channelId).isPresent();
    }
    @Override
    public void deleteAllByUserId(UUID userId) {
        Set<UUID> ids = byUser.remove(userId);
        if (ids == null) return;
        for (UUID id : ids) {
            ReadStatus rs = store.remove(id);
            if (rs == null) continue;
            Set<UUID> s = byChannel.get(rs.getChannelId());
            if (s != null) { s.remove(id); if (s.isEmpty()) byChannel.remove(rs.getChannelId()); }
        }
    }

    @Override
    public void deleteById(UUID id) {
        ReadStatus removed = store.remove(id);
        if (removed == null) return;

        Set<UUID> chSet = byChannel.get(removed.getChannelId());
        if (chSet != null) { chSet.remove(id); if (chSet.isEmpty()) byChannel.remove(removed.getChannelId()); }

        Set<UUID> usSet = byUser.get(removed.getUserId());
        if (usSet != null) { usSet.remove(id); if (usSet.isEmpty()) byUser.remove(removed.getUserId()); }
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        Set<UUID> ids = byChannel.getOrDefault(channelId, Collections.emptySet());
        return ids.stream().map(store::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        Set<UUID> ids = byChannel.remove(channelId);
        if (ids == null) return;
        for (UUID id : ids) {
            ReadStatus removed = store.remove(id);
            if (removed == null) continue;
            Set<UUID> usSet = byUser.get(removed.getUserId());
            if (usSet != null) { usSet.remove(id); if (usSet.isEmpty()) byUser.remove(removed.getUserId()); }
        }
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        // user 인덱스 ∩ channel 인덱스 교집합으로 탐색 (위와 반대 순서)
        Set<UUID> a = byUser.getOrDefault(userId, Collections.emptySet());
        Set<UUID> b = byChannel.getOrDefault(channelId, Collections.emptySet());
        for (UUID rsId : a) {
            if (b.contains(rsId)) return Optional.ofNullable(store.get(rsId));
        }
        return Optional.empty();
    }
}
