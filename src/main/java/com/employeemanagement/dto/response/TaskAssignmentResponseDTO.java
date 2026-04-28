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
public class TaskAssignmentResponseDTO {
    private Long assignmentId;
    private String roleInTask;
    private LocalDate assignmentDate;
    private Long employeeId;
    private String employeeFullName;
    private Long taskId;
    private String taskTitle;
}