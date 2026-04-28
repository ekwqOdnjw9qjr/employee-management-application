package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.EmployeeHistoryRequestDTO;
import com.employeemanagement.dto.response.EmployeeHistoryResponseDTO;
import com.employeemanagement.entity.EmployeeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeHistoryMapper {

    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(target = "employeeFullName", expression = "java(history.getEmployee().getLastName() + \" \" + history.getEmployee().getFirstName())")
    EmployeeHistoryResponseDTO toResponseDto(EmployeeHistory history);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "historyId", ignore = true)
    EmployeeHistory toEntity(EmployeeHistoryRequestDTO requestDTO);

    List<EmployeeHistoryResponseDTO> toResponseDtoList(List<EmployeeHistory> histories);
}