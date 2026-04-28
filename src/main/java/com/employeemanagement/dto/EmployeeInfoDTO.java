package com.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoDTO {
    private String fullName;
    private String position;
    private String contactPhone;
    private BigDecimal salary;
    private LocalDate hireDate;
    private String office;
    private String departmentName;
    private Integer floor;
}