package com.fullstack.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyCountDTO {

    private LocalDate date;
    private long count;

}
