package com.employeemanagement.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoEmployeeByFloor {
    private String departmentName;
    private Integer floor;
    private String fullName;
    private String position;
    private String office;
}