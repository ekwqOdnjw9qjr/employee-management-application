package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.TaskAssignmentRequestDTO;
import com.employeemanagement.dto.response.TaskAssignmentResponseDTO;
import com.employeemanagement.entity.TaskAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskAssignmentMapper {

    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "task.title", target = "taskTitle")
    @Mapping(target = "employeeFullName", expression = "java(assignment.getEmployee().getLastName() + \" \" + assignment.getEmployee().getFirstName())")
    TaskAssignmentResponseDTO toResponseDto(TaskAssignment assignment);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "assignmentId", ignore = true)
    TaskAssignment toEntity(TaskAssignmentRequestDTO requestDTO);

    List<TaskAssignmentResponseDTO> toResponseDtoList(List<TaskAssignment> assignments);
}