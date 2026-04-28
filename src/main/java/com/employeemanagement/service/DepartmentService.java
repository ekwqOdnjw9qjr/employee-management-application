package com.employeemanagement.service;

import com.employeemanagement.dto.request.DepartmentRequestDTO;
import com.employeemanagement.dto.response.DepartmentResponseDTO;
import com.employeemanagement.entity.Department;
import com.employeemanagement.mapper.DepartmentMapper;
import com.employeemanagement.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public List<DepartmentResponseDTO> getAllDepartments() {
        log.info("Fetching all departments");
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(departmentMapper::toResponseDtoWithCount)
                .toList();
    }

    public DepartmentResponseDTO getDepartmentById(Long id) {
        log.info("Fetching department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        return departmentMapper.toResponseDtoWithCount(department);
    }

    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        log.info("Creating new department: {}", requestDTO.getName());
        Department department = departmentMapper.toEntity(requestDTO);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponseDtoWithCount(saved);
    }

    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {
        log.info("Updating department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setName(requestDTO.getName());
        department.setFloor(requestDTO.getFloor());

        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponseDtoWithCount(updated);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}