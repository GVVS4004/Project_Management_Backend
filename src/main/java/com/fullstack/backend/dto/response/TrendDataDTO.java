package com.fullstack.backend.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class TrendDataDTO {
    private int period;
    private List<DailyCountDTO> tasksCompleted;
    private List<DailyCountDTO> tasksCreated;
}
