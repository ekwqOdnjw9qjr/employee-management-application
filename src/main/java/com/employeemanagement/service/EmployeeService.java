package com.employeemanagement.service;


import com.employeemanagement.dto.request.EmployeeRequestDTO;
import com.employeemanagement.dto.response.EmployeeResponseDTO;
import com.employeemanagement.entity.Department;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.mapper.EmployeeMapper;
import com.employeemanagement.repository.DepartmentRepository;
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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeResponseDTO> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeMapper.toResponseDtoList(employeeRepository.findAll());
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return employeeMapper.toResponseDto(employee);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        log.info("Creating employee: {} {}", requestDTO.getFirstName(), requestDTO.getLastName());

        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee employee = employeeMapper.toEntity(requestDTO);
        employee.setDepartment(department);

        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(saved);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        log.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employee.setLastName(requestDTO.getLastName());
        employee.setFirstName(requestDTO.getFirstName());
        employee.setMiddleName(requestDTO.getMiddleName());
        employee.setPosition(requestDTO.getPosition());
        employee.setPhone(requestDTO.getPhone());
        employee.setSalary(requestDTO.getSalary());
        employee.setHireDate(requestDTO.getHireDate());
        employee.setPhoto(requestDTO.getPhoto());
        employee.setOffice(requestDTO.getOffice());
        employee.setDepartment(department);

        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }
}