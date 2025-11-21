package com.sprint.mission.discodeit.entity.dto;

public record Pageable(
    Integer size,
    Integer page,
    String sort
) { }
