package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryOwnerType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Primary
public class JCFBinaryContentRepository implements BinaryContentRepository {

    // 메모리 저장소
    private final Map<UUID, BinaryContent> store = new ConcurrentHashMap<>();

    // ✅ 메시지 첨부(메시지ID -> 바이너리ID들)
    private final Map<UUID, Set<UUID>> byMessage = new ConcurrentHashMap<>();

    // ✅ 사용자 프로필(유저ID -> 바이너리ID) — 단일 매핑
    private final Map<UUID, UUID> profileByUser = new ConcurrentHashMap<>();

    @Override
    public BinaryContent save(BinaryContent binary) {
        store.put(binary.getId(), binary);

        if (binary.getOwnerType() == BinaryOwnerType.MESSAGE_ATTACHMENT) {
            byMessage.computeIfAbsent(binary.getOwnerId(), k -> new HashSet<>())
                    .add(binary.getId());
        } else if (binary.getOwnerType() == BinaryOwnerType.USER_PROFILE) {
            // 한 유저당 1개의 프로필만 관리 (덮어쓰기)
            profileByUser.put(binary.getOwnerId(), binary.getId());
        }
        return binary;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    // ✅ 유저 프로필 조회
    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        UUID binId = profileByUser.get(userId);
        return Optional.ofNullable(binId).map(store::get);
    }

    // (이미 사용 중이라면 유지)
    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        Set<UUID> ids = byMessage.getOrDefault(messageId, Collections.emptySet());
        return ids.stream().map(store::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    // (이미 사용 중이라면 유지)
    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return ids.stream().map(store::get).filter(Objects::nonNull).toList();
    }

    @Override
    public void deleteById(UUID id) {
        BinaryContent removed = store.remove(id);
        if (removed == null) return;

        if (removed.getOwnerType() == BinaryOwnerType.MESSAGE_ATTACHMENT) {
            Set<UUID> s = byMessage.get(removed.getOwnerId());
            if (s != null) {
                s.remove(id);
                if (s.isEmpty()) byMessage.remove(removed.getOwnerId());
            }
        } else if (removed.getOwnerType() == BinaryOwnerType.USER_PROFILE) {
            // 유저 프로필 인덱스에서 제거
            profileByUser.remove(removed.getOwnerId(), id);
        }
    }

    // ✅ 유저 프로필 삭제
    @Override
    public void deleteByUserId(UUID userId) {
        UUID binId = profileByUser.remove(userId);
        if (binId != null) {
            store.remove(binId);
        }
    }

    // (이미 사용 중이라면 유지)
    @Override
    public void deleteAllByMessageId(UUID messageId) {
        Set<UUID> ids = byMessage.remove(messageId);
        if (ids == null) return;
        for (UUID id : ids) {
            BinaryContent bc = store.remove(id);
            // MESSAGE_ATTACHMENT만 들어있어야 하지만 안전 차원에서 체크
            if (bc != null && bc.getOwnerType() == BinaryOwnerType.USER_PROFILE) {
                profileByUser.remove(bc.getOwnerId(), id);
            }
        }
    }
}
