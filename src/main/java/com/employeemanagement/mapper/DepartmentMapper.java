package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.DepartmentRequestDTO;
import com.employeemanagement.dto.response.DepartmentResponseDTO;
import com.employeemanagement.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponseDTO toResponseDto(Department department);

    Department toEntity(DepartmentRequestDTO requestDTO);

    @Mapping(target = "employeeCount", expression = "java(department.getEmployees() != null ? department.getEmployees().size() : 0)")
    DepartmentResponseDTO toResponseDtoWithCount(Department department);
}