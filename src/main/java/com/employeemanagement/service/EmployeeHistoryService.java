package com.employeemanagement.service;

import com.employeemanagement.dto.request.EmployeeHistoryRequestDTO;
import com.employeemanagement.dto.response.EmployeeHistoryResponseDTO;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.EmployeeHistory;
import com.employeemanagement.mapper.EmployeeHistoryMapper;
import com.employeemanagement.repository.EmployeeHistoryRepository;
import com.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeHistoryService {

    private final EmployeeHistoryRepository historyRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryMapper historyMapper;

    public List<EmployeeHistoryResponseDTO> getAllHistories() {
        log.info("Fetching all employee histories");
        List<EmployeeHistory> histories = historyRepository.findAll();
        return historyMapper.toResponseDtoList(histories);
    }

    public EmployeeHistoryResponseDTO getHistoryById(Long id) {
        log.info("Fetching history with id: {}", id);
        EmployeeHistory history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found with id: " + id));
        return historyMapper.toResponseDto(history);
    }

    @Transactional
    public EmployeeHistoryResponseDTO createHistory(EmployeeHistoryRequestDTO requestDTO) {
        log.info("Creating new employee history record");

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        EmployeeHistory history = historyMapper.toEntity(requestDTO);
        history.setEmployee(employee);

        EmployeeHistory savedHistory = historyRepository.save(history);
        return historyMapper.toResponseDto(savedHistory);
    }

    @Transactional
    public EmployeeHistoryResponseDTO updateHistory(Long id, EmployeeHistoryRequestDTO requestDTO) {
        log.info("Updating history with id: {}", id);

        EmployeeHistory history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found with id: " + id));

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + requestDTO.getEmployeeId()));

        history.setChangeDate(requestDTO.getChangeDate());
        history.setChangeType(requestDTO.getChangeType());
        history.setEmployee(employee);

        EmployeeHistory updatedHistory = historyRepository.save(history);
        return historyMapper.toResponseDto(updatedHistory);
    }

    @Transactional
    public void deleteHistory(Long id) {
        log.info("Deleting history with id: {}", id);
        if (!historyRepository.existsById(id)) {
            throw new RuntimeException("History not found with id: " + id);
        }
        historyRepository.deleteById(id);
    }
}