package com.employeemanagement.service;


import com.employeemanagement.dto.request.TaskRequestDTO;
import com.employeemanagement.dto.response.TaskResponseDTO;
import com.employeemanagement.entity.Task;
import com.employeemanagement.mapper.TaskMapper;
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
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskResponseDTO> getAllTasks() {
        log.info("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return taskMapper.toResponseDto(task);
    }

    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        log.info("Creating new task: {}", requestDTO.getTitle());
        Task task = taskMapper.toEntity(requestDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDto(savedTask);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO) {
        log.info("Updating task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setStartDate(requestDTO.getStartDate());
        task.setDeadline(requestDTO.getDeadline());
        task.setStatus(requestDTO.getStatus());

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponseDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}