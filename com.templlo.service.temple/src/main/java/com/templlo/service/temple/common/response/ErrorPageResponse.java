package com.templlo.service.temple.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Pageable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorPageResponse(String message,
                                int pageNumber,
                                int pageSize) {

    public static ErrorPageResponse of(String message, Pageable pageable) {
        return new ErrorPageResponse(
                message,
                pageable.getPageNumber() + 1,
                pageable.getPageSize()
        );
    }
}
