package com.employeemanagement.service;

import com.employeemanagement.dto.request.PerformanceEvaluationRequestDTO;
import com.employeemanagement.dto.response.PerformanceEvaluationResponseDTO;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.PerformanceEvaluation;
import com.employeemanagement.mapper.PerformanceEvaluationMapper;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.PerformanceEvaluationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PerformanceEvaluationService {

    private final PerformanceEvaluationRepository evaluationRepository;
    private final EmployeeRepository employeeRepository;
    private final PerformanceEvaluationMapper evaluationMapper;

    public List<PerformanceEvaluationResponseDTO> getAllEvaluations() {
        log.info("Fetching all performance evaluations");
        List<PerformanceEvaluation> evaluations = evaluationRepository.findAll();
        return evaluationMapper.toResponseDtoList(evaluations);
    }

    public PerformanceEvaluationResponseDTO getEvaluationById(Long id) {
        log.info("Fetching evaluation with id: {}", id);
        PerformanceEvaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation not found with id: " + id));
        return evaluationMapper.toResponseDto(evaluation);
    }



    @Transactional
    public PerformanceEvaluationResponseDTO createEvaluation(PerformanceEvaluationRequestDTO requestDTO) {
        log.info("Creating new performance evaluation");

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        Employee reviewer = employeeRepository.findById(requestDTO.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + requestDTO.getReviewerId()));

        PerformanceEvaluation evaluation = evaluationMapper.toEntity(requestDTO);
        evaluation.setEmployee(employee);
        evaluation.setReviewer(reviewer);

        PerformanceEvaluation savedEvaluation = evaluationRepository.save(evaluation);
        return evaluationMapper.toResponseDto(savedEvaluation);
    }

    @Transactional
    public PerformanceEvaluationResponseDTO updateEvaluation(Long id, PerformanceEvaluationRequestDTO requestDTO) {
        log.info("Updating evaluation with id: {}", id);

        PerformanceEvaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation not found with id: " + id));

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        Employee reviewer = employeeRepository.findById(requestDTO.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + requestDTO.getReviewerId()));

        evaluation.setEvaluationDate(requestDTO.getEvaluationDate());
        evaluation.setKpi(requestDTO.getKpi());
        evaluation.setScore(requestDTO.getScore());
        evaluation.setComment(requestDTO.getComment());
        evaluation.setEmployee(employee);
        evaluation.setReviewer(reviewer);

        PerformanceEvaluation updatedEvaluation = evaluationRepository.save(evaluation);
        return evaluationMapper.toResponseDto(updatedEvaluation);
    }

    @Transactional
    public void deleteEvaluation(Long id) {
        log.info("Deleting evaluation with id: {}", id);
        if (!evaluationRepository.existsById(id)) {
            throw new RuntimeException("Evaluation not found with id: " + id);
        }
        evaluationRepository.deleteById(id);
    }
}