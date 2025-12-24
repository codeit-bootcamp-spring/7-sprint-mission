package com.sprint.mission.discodeit.dto.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record PageRequest(
        @NotNull
        Integer size,

        @NotNull
        Integer page,

        @NotNull
        Sort sort
) {
    @JsonCreator
    public PageRequest(
            @JsonProperty("size") Integer size,
            @JsonProperty("page") Integer page,
            @JsonProperty("sort") String sort
    ) {
        this(
                size,
                page,
                convertToSort(sort)
        );
    }

    private static Sort convertToSort(String sort) {
        String[] split = sort.split(",");
        return switch (split[1].toLowerCase()) {
            case "desc" -> Sort.by(split[0]).descending();
            case "asc" -> Sort.by(split[0]).ascending();
            default -> throw new IllegalStateException("Unexpected value: " + split[1].toLowerCase());
        };
    }
}
