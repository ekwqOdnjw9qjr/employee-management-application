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
public class TrainingResponseDTO {
    private Integer trainingId;
    private String courseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String organization;
    private Long employeeId;
    private String employeeFullName;
}