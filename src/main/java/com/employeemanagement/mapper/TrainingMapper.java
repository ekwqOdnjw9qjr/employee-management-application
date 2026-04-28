package com.employeemanagement.mapper;

import com.employeemanagement.dto.request.TrainingRequestDTO;
import com.employeemanagement.dto.response.TrainingResponseDTO;
import com.employeemanagement.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(target = "employeeFullName", expression = "java(training.getEmployee().getLastName() + \" \" + training.getEmployee().getFirstName())")
    TrainingResponseDTO toResponseDto(Training training);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "trainingId", ignore = true)
    Training toEntity(TrainingRequestDTO requestDTO);

    List<TrainingResponseDTO> toResponseDtoList(List<Training> trainings);
}