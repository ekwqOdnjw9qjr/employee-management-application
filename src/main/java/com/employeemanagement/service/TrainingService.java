package com.employeemanagement.service;

import com.employeemanagement.dto.request.TrainingRequestDTO;
import com.employeemanagement.dto.response.TrainingResponseDTO;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.Training;
import com.employeemanagement.mapper.TrainingMapper;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final EmployeeRepository employeeRepository;
    private final TrainingMapper trainingMapper;

    public List<TrainingResponseDTO> getAllTrainings() {
        log.info("Fetching all trainings");
        List<Training> trainings = trainingRepository.findAll();
        return trainingMapper.toResponseDtoList(trainings);
    }

    public TrainingResponseDTO getTrainingById(Integer id) {
        log.info("Fetching training with id: {}", id);
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training not found with id: " + id));
        return trainingMapper.toResponseDto(training);
    }

    @Transactional
    public TrainingResponseDTO createTraining(TrainingRequestDTO requestDTO) {
        log.info("Creating new training: {}", requestDTO.getCourseName());

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        Training training = trainingMapper.toEntity(requestDTO);
        training.setEmployee(employee);

        Training savedTraining = trainingRepository.save(training);
        return trainingMapper.toResponseDto(savedTraining);
    }

    @Transactional
    public TrainingResponseDTO updateTraining(Integer id, TrainingRequestDTO requestDTO) {
        log.info("Updating training with id: {}", id);

        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training not found with id: " + id));

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        training.setCourseName(requestDTO.getCourseName());
        training.setStartDate(requestDTO.getStartDate());
        training.setEndDate(requestDTO.getEndDate());
        training.setStatus(requestDTO.getStatus());
        training.setOrganization(requestDTO.getOrganization());
        training.setEmployee(employee);

        Training updatedTraining = trainingRepository.save(training);
        return trainingMapper.toResponseDto(updatedTraining);
    }

    @Transactional
    public void deleteTraining(Integer id) {
        log.info("Deleting training with id: {}", id);
        if (!trainingRepository.existsById(id)) {
            throw new RuntimeException("Training not found with id: " + id);
        }
        trainingRepository.deleteById(id);
    }
}