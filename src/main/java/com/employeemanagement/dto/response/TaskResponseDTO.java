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
public class TaskResponseDTO {
    private Long taskId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate deadline;
    private String status;
    private Integer assignedEmployeesCount;
}