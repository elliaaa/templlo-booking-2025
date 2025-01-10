package com.templlo.service.reservation.global;


import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public class PageUtil {
    private static final int DEFAULT_PAGE_NUM = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_BY = SortBy.CREATED_AT.fieldName;
    private static final List<Integer> AVAILABLE_SIZE_LIST = Arrays.asList(DEFAULT_SIZE, 30, 50);

    public static Pageable getCheckedPageable(Pageable pageable) {
        int page = pageable.getPageNumber();
        page = Math.max(page, DEFAULT_PAGE_NUM);

        int size = pageable.getPageSize();
        if (!AVAILABLE_SIZE_LIST.contains(size)) {
            size = DEFAULT_SIZE;
        }

        Sort sort = pageable.getSort();
        Sort.Order order = sort.stream().iterator().next(); // TODO : 없을 경우 예외처리
        String sortBy = order.getProperty();
        if (!SortBy.isValid(sortBy)) {
            sortBy = DEFAULT_SORT_BY;
        }

        return PageRequest.of(page, size, Sort.by(sortBy).descending());
    }

    private enum SortBy {
        CREATED_AT("createdAt"),
        UPDATED_AT("updatedAt");

        private final String fieldName;

        SortBy(String fieldName) {
            this.fieldName = fieldName;
        }

        public static boolean isValid(String sortBy) {
            return Arrays.stream(SortBy.values())
                    .anyMatch(s -> s.fieldName.equals(sortBy));
        }
    }
}
