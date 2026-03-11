package com.fullstack.backend.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private PaginationUtils() {}

    /**
     * Ensures stable pagination by appending "id" as a tiebreaker sort.
     * Without this, rows with identical sort values (e.g., same createdAt)
     * can appear on multiple pages or be skipped entirely.
     */
    public static Pageable ensureStableSort(Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort.getOrderFor("id") != null) {
            return pageable; // Already has id in sort, no change needed
        }
        Sort stableSort = sort.isSorted()
                ? sort.and(Sort.by("id"))
                : Sort.by("id");
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), stableSort);
    }
}
