package com.employeemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceEvaluationRequestDTO {

    @NotNull(message = "Evaluation date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate evaluationDate;

    @NotBlank(message = "KPI is required")
    private String kpi;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must not exceed 100")
    private Integer score;

    private String comment;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;
}