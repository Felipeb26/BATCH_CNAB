package com.batsworks.batch.domain.records;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageDTO<T, E>(
        List<E> content,
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last,
        int totalPages,
        int totalElements,
        int numberOfElements
) {

    public PageDTO(Page<T> page, Function<List<T>, List<E>> function) {
        this(function.apply(page.getContent()), page.getNumber(), page.getSize(), page.isFirst(), page.isLast(), page.getTotalPages(), page.getSize(), page.getNumberOfElements());

    }
}
