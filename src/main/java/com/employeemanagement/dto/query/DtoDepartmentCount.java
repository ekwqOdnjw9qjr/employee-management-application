package com.employeemanagement.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoDepartmentCount {
    private String departmentName;
    private Long employeeCount;
}