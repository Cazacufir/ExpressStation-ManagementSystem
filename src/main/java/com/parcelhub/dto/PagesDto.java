package com.parcelhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagesDto<T> {
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int currentElements;
    private int pageSize;
    private boolean hasMore;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasPrevious;
    private List<T> dataList;
}

