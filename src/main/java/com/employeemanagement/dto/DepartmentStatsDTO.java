package com.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStatsDTO {
    private String description;
    private Long totalEmployees;
    private BigDecimal totalSalaryFund;
}