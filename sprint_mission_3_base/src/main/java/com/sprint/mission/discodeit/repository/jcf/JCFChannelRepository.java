package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JCF 기반 인메모리 채널 저장소
 * - 채널 저장/조회
 * - PRIVATE 채널의 참여자 인덱스 유지
 */
@Repository
@Primary // 여러 구현이 동시에 빈으로 뜨는 경우 이 구현을 기본으로 사용
public class JCFChannelRepository implements ChannelRepository {

    // 채널 본문 저장소
    private final Map<UUID, Channel> store = new ConcurrentHashMap<>();

    // 참여자 인덱스 (channelId -> userIds)
    private final Map<UUID, Set<UUID>> participantsByChannel = new ConcurrentHashMap<>();
    // 역인덱스 (userId -> channelIds)
    private final Map<UUID, Set<UUID>> channelIdsByUser = new ConcurrentHashMap<>();

    @Override
    public Channel save(Channel c) {
        store.put(c.getId(), c);   // 저장/조회 키는 항상 getId()
        return c;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(UUID id) {
        // 본문 삭제
        store.remove(id);
        // 참여자 인덱스 정리
        Set<UUID> users = participantsByChannel.remove(id);
        if (users != null) {
            for (UUID u : users) {
                Set<UUID> chs = channelIdsByUser.get(u);
                if (chs != null) {
                    chs.remove(id);
                    if (chs.isEmpty()) channelIdsByUser.remove(u);
                }
            }
        }
    }

    @Override
    public List<Channel> findAllPublic() {
        return List.of();
    }

    /* ====== PRIVATE 채널 전용 헬퍼 ====== */

    /** PRIVATE 채널의 참여자 목록 저장/치환 */
    @Override
    public void setParticipants(UUID channelId, Collection<UUID> userIds) {
        // 기존 역인덱스 제거
        Set<UUID> old = participantsByChannel.get(channelId);
        if (old != null) {
            for (UUID u : old) {
                Set<UUID> chs = channelIdsByUser.get(u);
                if (chs != null) {
                    chs.remove(channelId);
                    if (chs.isEmpty()) channelIdsByUser.remove(u);
                }
            }
        }
        // 신규 세팅
        Set<UUID> copy = new HashSet<>(userIds);
        participantsByChannel.put(channelId, copy);
        for (UUID u : copy) {
            channelIdsByUser.computeIfAbsent(u, k -> new HashSet<>()).add(channelId);
        }
    }

    /** 특정 PRIVATE 채널의 참여자 id 목록 */
    @Override
    public List<UUID> participantUserIds(UUID channelId) {
        return new ArrayList<>(
                participantsByChannel.getOrDefault(channelId, Collections.emptySet())
        );
    }

    /** 유저가 참가한 PRIVATE 채널 전부 */
    @Override
    public List<Channel> findAllPrivateByUserId(UUID userId) {
        Set<UUID> chIds = channelIdsByUser.getOrDefault(userId, Collections.emptySet());
        if (chIds.isEmpty()) return Collections.emptyList();

        List<Channel> result = new ArrayList<>(chIds.size());
        for (UUID id : chIds) {
            Channel c = store.get(id);
            if (c != null && c.getVisibility() == Channel.Visibility.PRIVATE) {
                result.add(c);
            }
        }
        return result;
    }
}
