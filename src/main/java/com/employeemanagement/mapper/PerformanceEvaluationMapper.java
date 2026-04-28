package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.PerformanceEvaluationRequestDTO;
import com.employeemanagement.dto.response.PerformanceEvaluationResponseDTO;
import com.employeemanagement.entity.PerformanceEvaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PerformanceEvaluationMapper {

    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(source = "reviewer.employeeId", target = "reviewerId")
    @Mapping(target = "employeeFullName", expression = "java(evaluation.getEmployee().getLastName() + \" \" + evaluation.getEmployee().getFirstName())")
    @Mapping(target = "reviewerFullName", expression = "java(evaluation.getReviewer().getLastName() + \" \" + evaluation.getReviewer().getFirstName())")
    PerformanceEvaluationResponseDTO toResponseDto(PerformanceEvaluation evaluation);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "evaluationId", ignore = true)
    PerformanceEvaluation toEntity(PerformanceEvaluationRequestDTO requestDTO);

    List<PerformanceEvaluationResponseDTO> toResponseDtoList(List<PerformanceEvaluation> evaluations);
}