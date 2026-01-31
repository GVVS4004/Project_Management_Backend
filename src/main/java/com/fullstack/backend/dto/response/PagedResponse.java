package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse <T>{
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private List<T> content;

    public static <T> PagedResponse<T> of(Page<T> page){
        return new PagedResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.getContent()
        );
    }

    public static <T> PagedResponse<T> empty(){
        return new PagedResponse<>(
                0,
                0,
                0L,
                0,
                true,
                true,
                List.of()
        );
    }

}
