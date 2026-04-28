package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.EmployeeRequestDTO;
import com.employeemanagement.dto.response.EmployeeResponseDTO;
import com.employeemanagement.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "department.departmentId", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "department.floor", target = "floor")
    EmployeeResponseDTO toResponseDto(Employee employee);

    @Mapping(target = "department", ignore = true)
    Employee toEntity(EmployeeRequestDTO requestDTO);
    List<EmployeeResponseDTO> toResponseDtoList(List<Employee> employees);
}