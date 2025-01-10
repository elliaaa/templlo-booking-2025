package com.templlo.service.temple.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>( int totalPages,
                               int pageNumber,
                               List<T> content) {

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getContent()
        );
    }
}
