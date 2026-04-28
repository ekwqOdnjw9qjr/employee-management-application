package com.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceEvaluationResponseDTO {
    private Long evaluationId;
    private LocalDate evaluationDate;
    private String kpi;
    private Integer score;
    private String comment;
    private Long employeeId;
    private String employeeFullName;
    private Long reviewerId;
    private String reviewerFullName;
}