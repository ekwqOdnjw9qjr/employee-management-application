package com.employeemanagement.service;

import com.employeemanagement.dto.request.TaskAssignmentRequestDTO;
import com.employeemanagement.dto.response.TaskAssignmentResponseDTO;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.Task;
import com.employeemanagement.entity.TaskAssignment;
import com.employeemanagement.mapper.TaskAssignmentMapper;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.TaskAssignmentRepository;
import com.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskAssignmentService {

    private final TaskAssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final TaskAssignmentMapper assignmentMapper;

    public List<TaskAssignmentResponseDTO> getAllAssignments() {
        log.info("Fetching all task assignments");
        List<TaskAssignment> assignments = assignmentRepository.findAll();
        return assignmentMapper.toResponseDtoList(assignments);
    }

    public TaskAssignmentResponseDTO getAssignmentById(Long id) {
        log.info("Fetching assignment with id: {}", id);
        TaskAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        return assignmentMapper.toResponseDto(assignment);
    }

    @Transactional
    public TaskAssignmentResponseDTO createAssignment(TaskAssignmentRequestDTO requestDTO) {
        log.info("Creating new task assignment");

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        Task task = taskRepository.findById(requestDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + requestDTO.getTaskId()));

        TaskAssignment assignment = assignmentMapper.toEntity(requestDTO);
        assignment.setEmployee(employee);
        assignment.setTask(task);

        TaskAssignment savedAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toResponseDto(savedAssignment);
    }

    @Transactional
    public TaskAssignmentResponseDTO updateAssignment(Long id, TaskAssignmentRequestDTO requestDTO) {
        log.info("Updating assignment with id: {}", id);

        TaskAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        Task task = taskRepository.findById(requestDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + requestDTO.getTaskId()));

        assignment.setRoleInTask(requestDTO.getRoleInTask());
        assignment.setAssignmentDate(requestDTO.getAssignmentDate());
        assignment.setEmployee(employee);
        assignment.setTask(task);

        TaskAssignment updatedAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toResponseDto(updatedAssignment);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        log.info("Deleting assignment with id: {}", id);
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }
}