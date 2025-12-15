package com.sprint.mission.discodeit.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private T nextCursor;
    private Integer size;
    private boolean hasNext;
    private Long totalElements;
}
