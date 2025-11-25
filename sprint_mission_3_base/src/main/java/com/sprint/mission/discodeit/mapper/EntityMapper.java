package com.sprint.mission.discodeit.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class EntityMapper {

    public <T, R> R toDto(T entity, Function<T, R> mapper) {
        return mapper.apply(entity);
    }

    public <T, R> List<R> toDtoList(List<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).toList();
    }
}
