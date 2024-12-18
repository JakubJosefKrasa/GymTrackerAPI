package com.kuba.gymtrackerapi.pagination;

import java.util.List;

public record PaginationDTO<T>(
        List<T> items,
        long totalItems,
        boolean hasPreviousPage,
        boolean hasNextPage
) {
}
