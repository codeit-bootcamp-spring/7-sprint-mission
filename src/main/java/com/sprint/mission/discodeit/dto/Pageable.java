package com.sprint.mission.discodeit.dto;

public record Pageable(
    Integer size,
    Integer page,
    String sort
) { }
