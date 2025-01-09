package com.templlo.service.reservation.global.common.response;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import java.util.List;

public record PageResponse<T>(
        int totalPage,
        int pageNumber,
        int size,
        List<T> content
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getSize(),
                page.getContent()
        );
    }

    public static <T> PageResponse<T> of(PagedModel<T> pagedModel) {
        return new PageResponse<>(
                Long.valueOf(pagedModel.getMetadata().totalPages()).intValue(),
                Long.valueOf(pagedModel.getMetadata().number() + 1).intValue(),
                Long.valueOf(pagedModel.getMetadata().size()).intValue(),
                pagedModel.getContent()
        );
    }
}