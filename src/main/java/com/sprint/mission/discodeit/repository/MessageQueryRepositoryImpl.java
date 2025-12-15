package com.sprint.mission.discodeit.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.QChannel.*;
import static com.sprint.mission.discodeit.entity.QMessage.*;
import static com.sprint.mission.discodeit.entity.QMessageAttachment.*;
import static com.sprint.mission.discodeit.entity.QUser.*;

@Repository
@RequiredArgsConstructor
public class MessageQueryRepositoryImpl implements MessageQueryRepository {

    private final JPAQueryFactory query;

    public List<Message> findAllByChannelId(
            UUID channelId,
            int size,
            String sort,
            Instant cursor) {
        return query
                .select(message)
                .from(message)
                .join(message.user, user)
                .where(message.channel.id.eq(channelId), createdAtGt(cursor, sort))
                .orderBy(orderBy(sort))
                .limit(size)
                .fetch();
    }


    private BooleanExpression createdAtGt(Instant cursor, String sort) {
        if (cursor == null) return null;
        if (sort.equals("createdAt,asc")) {
            return message.createdAt.gt(cursor);
        }
        return message.createdAt.lt(cursor);
    }


    private OrderSpecifier<?>[] orderBy(String sort) {
        return switch (sort){
            case "createdAt,desc" -> new OrderSpecifier[]{message.createdAt.desc()};
            case "createdAt,asc" ->new OrderSpecifier[]{message.createdAt.asc()};
            default -> throw new IllegalArgumentException("지원하지 않는 정렬: " + sort);
        };

    }

    public Long getTotalElementsByChannelId(UUID channelId) {
        return query
                .select(message.count())
                .from(message)
                .where(message.channel.id.eq(channelId))
                .fetchOne();
    }
}
