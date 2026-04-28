package com.employeemanagement.service;

import com.employeemanagement.dto.*;
import com.employeemanagement.dto.query.*;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.NativeQueryRepository;
import com.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DatabaseQueryService {

    private final NativeQueryRepository nativeQueryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public List<DtoEmployeeOrder> getEmployeesOrdered() {
        log.info("Getting employees ordered by department and name");
        return nativeQueryRepository.getEmployeesOrdered();
    }

    public List<DtoEmployeeByFloor> getEmployeesByFloor(Integer floor) {
        log.info("Getting employees by floor: {}", floor);
        return nativeQueryRepository.getEmployeesByFloor(floor);
    }

    public List<DtoEmployeeFullInfo> getEmployeeFullInfo(String lastName, String firstName, String middleName) {
        log.info("Getting full info for employee: {} {} {}", lastName, firstName, middleName);
        return nativeQueryRepository.getEmployeeFullInfo(lastName, firstName, middleName);
    }

    public List<DtoEmployeeContact> getEmployeeContact(String lastName) {
        log.info("Getting contact info for employee: {}", lastName);
        return nativeQueryRepository.getEmployeeContact(lastName);
    }

    public List<DtoDepartmentCount> getEmployeeCountByDepartment() {
        log.info("Getting employee count by department");
        return nativeQueryRepository.getEmployeeCountByDepartment();
    }

    public List<DtoSalaryFund> getSalaryFundByDepartment() {
        log.info("Getting salary fund by department");
        return nativeQueryRepository.getSalaryFundByDepartment();
    }

    @Transactional
    public int deleteFiredEmployees() {
        log.info("Deleting fired employees");
        return nativeQueryRepository.deleteFiredEmployees();
    }


    // 1. Процедура ordering_by_fields
    public List<EmployeeOrderDTO> callOrderingByFields(String orderBy) {
        log.info("Calling stored procedure ordering_by_fields with param: {}", orderBy);

        String sql = "SELECT * FROM ordering_by_fields(:orderBy)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("orderBy", orderBy);

        List<Object[]> results = query.getResultList();
        log.info("Results count: {}", results.size());

        List<EmployeeOrderDTO> list = new ArrayList<>();
        for (Object[] row : results) {
            EmployeeOrderDTO dto = new EmployeeOrderDTO();
            dto.setDepartmentName(row[0] != null ? row[0].toString() : "");
            dto.setFullName(row[1] != null ? row[1].toString() : "");
            dto.setPosition(row[2] != null ? row[2].toString() : "");
            dto.setPhone(row[3] != null ? row[3].toString() : "");
            dto.setSalary(row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO);
            dto.setFloor(row[5] != null ? ((Number) row[5]).intValue() : 0);
            list.add(dto);
        }
        return list;
    }

    // 2. Процедура find_employees_by_floor
    @SuppressWarnings("unchecked")
    public List<EmployeeByFloorDTO> callFindEmployeesByFloor(Integer floor) {
        log.info("Calling stored procedure find_employees_by_floor with floor: {}", floor);

        String sql = "SELECT * FROM find_employees_by_floor(?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, floor);

        List<Object[]> results = query.getResultList();

        log.info("Results count: {}", results.size());

        return results.stream()
                .map(row -> {
                    try {
                        return new EmployeeByFloorDTO(
                                row[0] != null ? row[0].toString() : "",
                                row[1] != null ? row[1].toString() : "",
                                row[2] != null ? row[2].toString() : "",
                                row[3] != null ? row[3].toString() : "",
                                row[4] != null ? ((Number) row[4]).intValue() : 0,
                                row[5] != null ? row[5].toString() : ""
                        );
                    } catch (Exception e) {
                        log.error("Error mapping row: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 3. Процедура get_employee_info
    @SuppressWarnings("unchecked")
    public List<EmployeeInfoDTO> callGetEmployeeInfo(String lastName, String firstName, String middleName) {
        log.info("Calling stored procedure get_employee_info with: {} {} {}", lastName, firstName, middleName);

        String sql = "SELECT * FROM get_employee_info(?, ?, ?)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, lastName);
        query.setParameter(2, firstName);
        query.setParameter(3, middleName);

        List<Object[]> results = query.getResultList();

        log.info("Results count: {}", results.size());

        return results.stream()
                .map(row -> {
                    try {
                        return new EmployeeInfoDTO(
                                row[0] != null ? row[0].toString() : "",
                                row[1] != null ? row[1].toString() : "",
                                row[2] != null ? row[2].toString() : "",
                                row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO,
                                row[4] != null ? ((java.sql.Date) row[4]).toLocalDate() : null,
                                row[5] != null ? row[5].toString() : "",
                                row[6] != null ? row[6].toString() : "",
                                row[7] != null ? ((Number) row[7]).intValue() : 0
                        );
                    } catch (Exception e) {
                        log.error("Error mapping row: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 4. Процедура calculate_department_stats
    @SuppressWarnings("unchecked")
    public DepartmentStatsDTO callCalculateDepartmentStats() {
        log.info("Calling stored procedure calculate_department_stats");

        String sql = "SELECT * FROM calculate_department_stats()";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> results = query.getResultList();

        log.info("Results count: {}", results.size());

        if (results.isEmpty()) {
            log.warn("No results from calculate_department_stats");
            return new DepartmentStatsDTO("Нет данных", 0L, BigDecimal.ZERO);
        }

        Object[] row = results.get(0);
        log.info("Row data: {}", Arrays.toString(row));

        return new DepartmentStatsDTO(
                row[0] != null ? row[0].toString() : "Нет данных",
                row[1] != null ? ((Number) row[1]).longValue() : 0L,
                row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO
        );
    }
}