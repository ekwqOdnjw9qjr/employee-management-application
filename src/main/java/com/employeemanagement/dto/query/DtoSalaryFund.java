package com.employeemanagement.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoSalaryFund {
    private String departmentName;
    private BigDecimal salaryFund;
}