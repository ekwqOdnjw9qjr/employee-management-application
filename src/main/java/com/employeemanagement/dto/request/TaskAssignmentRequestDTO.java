package com.employeemanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentRequestDTO {

    @NotNull(message = "Role in task is required")
    private String roleInTask;

    @NotNull(message = "Assignment date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Task ID is required")
    private Long taskId;
}