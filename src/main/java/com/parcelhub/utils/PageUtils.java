package com.parcelhub.utils;

import com.parcelhub.dto.PagesDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PageUtils {

    public static <T> PagesDto<T> listToPageDTO(List<T> userList, int page, int size) {
        int currentElements = page * size <= userList.size() ? size : (page == 1 && userList.size() < size) ? userList.size() : userList.size() % size;
        int totalPages = BigDecimal.valueOf(userList.size())
                .divide(BigDecimal.valueOf(size), RoundingMode.UP)
                .intValue();

        return new PagesDto<>(
                totalPages, // totalPages
                userList.size(), // totalElements, assuming it should be long
                page, // currentPage
                currentElements, // currentElements
                size, // pageSize
                page * size < userList.size(), // hasMore
                page == 1, // isFirst
                page == totalPages, // isLast
                page != 1, // hasPrevious
                userList // records
        );
    }
}
