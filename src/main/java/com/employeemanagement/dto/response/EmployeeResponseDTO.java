package com.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long employeeId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String position;
    private String phone;
    private BigDecimal salary;
    private LocalDate hireDate;
    private String photo;
    private String office;
    private Long departmentId;
    private String departmentName;
    private Integer floor;
}