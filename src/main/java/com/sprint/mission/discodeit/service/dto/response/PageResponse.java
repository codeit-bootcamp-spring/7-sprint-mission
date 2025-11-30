package com.sprint.mission.discodeit.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private Integer number;
    private Integer size;
    private boolean hasNext;
    private Long totalElements;
}
