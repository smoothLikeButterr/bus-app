package com.busQR.busApp.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PageResult<T> {
    private final List<T> content;
    private final long total;
    private final int page;
    private final int size;

    public long getTotalPages() {
        return size == 0 ? 1 : (total + size - 1) / size;
    }
}
