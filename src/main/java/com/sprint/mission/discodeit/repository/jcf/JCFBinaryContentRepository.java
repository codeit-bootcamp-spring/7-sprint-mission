package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent b) {
        data.put(b.getUserId(), b);
        return b;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    //====================================


    // 유저 프로필 관련은 messageId가 null
    @Override
    public Optional<BinaryContent> findProfileImageByUserId(UUID userId) {
        return data.values().stream()
                .filter(content -> content.getMessageId() == null)
                .filter(content -> content.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteProfileImageByUserId(UUID userId) {
        data.values().removeIf(profile ->
                profile.getUserId().equals(userId) && profile.getMessageId() == null);
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return data.values().stream()
                .filter(content -> content.getMessageId().equals(messageId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAllByMessageIdIn(List<UUID> messageIds) {
        Set<UUID> idSet = new HashSet<>(messageIds);
        return  data.values().stream()
                .filter(content -> idSet.contains(content.getMessageId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream().map(data::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByMessageId(UUID messageId) {
        data.values().removeIf(content -> content.getMessageId().equals(messageId));
    }
}
