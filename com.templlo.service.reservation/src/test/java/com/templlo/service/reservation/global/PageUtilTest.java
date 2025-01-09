package com.templlo.service.reservation.global;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class PageUtilTest {

    @Test
    void getCheckedPageableTest() {
        //given
        int page = 0;
        int size = 123123;
        String sortBy = "키키";
        Pageable pageableInput = PageRequest.of(page, size, Sort.by(sortBy).descending());

        //when
        Pageable pageableOutput = PageUtil.getCheckedPageable(pageableInput);

        //then
        assertEquals(pageableOutput.getPageNumber(), page);
        assertEquals(pageableOutput.getPageSize(), 10);

        Sort sort = pageableOutput.getSort();
        Sort.Order order = sort.stream().iterator().next();
        String sortByOutput = order.getProperty();
        assertEquals(sortByOutput, "createdAt");
    }
}