package com.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOrderDTO {
    private String departmentName;
    private String fullName;
    private String position;
    private String phone;
    private BigDecimal salary;
    private Integer floor;
}