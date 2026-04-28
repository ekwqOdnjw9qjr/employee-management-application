package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.TaskRequestDTO;
import com.employeemanagement.dto.response.TaskResponseDTO;
import com.employeemanagement.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assignedEmployeesCount", expression = "java(task.getAssignments() != null ? task.getAssignments().size() : 0)")
    TaskResponseDTO toResponseDto(Task task);

    Task toEntity(TaskRequestDTO requestDTO);

    List<TaskResponseDTO> toResponseDtoList(List<Task> tasks);
}