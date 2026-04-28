package com.employeemanagement.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoEmployeeFullInfo {
    private Long employeeId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String position;
    private String phone;
    private BigDecimal salary;
    private LocalDate hireDate;
    private String office;
    private String departmentName;
}