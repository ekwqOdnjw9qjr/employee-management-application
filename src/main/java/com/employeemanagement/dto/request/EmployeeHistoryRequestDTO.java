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
public class EmployeeHistoryRequestDTO {

    @NotNull(message = "Change date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate changeDate;

    @NotNull(message = "Change type is required")
    private String changeType;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;
}