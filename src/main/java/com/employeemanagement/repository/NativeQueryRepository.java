package com.employeemanagement.repository;

import com.employeemanagement.dto.query.*;
import com.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public interface NativeQueryRepository extends JpaRepository<Employee, Long> {

    @Query(value = """
        SELECT 
            d.name AS department_name,
            CONCAT(e.last_name, ' ', e.first_name, ' ', COALESCE(e.middle_name, '')) AS full_name,
            e.position AS position
        FROM employee e
        JOIN department d ON e.department_id = d.department_id
        ORDER BY d.name, e.last_name, e.first_name
    """, nativeQuery = true)
    List<Object[]> getEmployeesOrderedRaw();

    default List<DtoEmployeeOrder> getEmployeesOrdered() {
        return getEmployeesOrderedRaw().stream()
                .map(row -> new DtoEmployeeOrder(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2]
                ))
                .toList();
    }

    @Query(value = """
        SELECT 
            d.name AS department_name,
            d.floor AS floor,
            CONCAT(e.last_name, ' ', e.first_name, ' ', COALESCE(e.middle_name, '')) AS full_name,
            e.position AS position,
            e.office AS office
        FROM employee e
        JOIN department d ON e.department_id = d.department_id
        WHERE d.floor = :floor
    """, nativeQuery = true)
    List<Object[]> getEmployeesByFloorRaw(@Param("floor") Integer floor);

    default List<DtoEmployeeByFloor> getEmployeesByFloor(Integer floor) {
        return getEmployeesByFloorRaw(floor).stream()
                .map(row -> new DtoEmployeeByFloor(
                        (String) row[0],
                        ((Number) row[1]).intValue(),
                        (String) row[2],
                        (String) row[3],
                        (String) row[4]
                ))
                .toList();
    }

    @Query(value = """
    SELECT 
        e.employee_id AS employee_id,
        e.last_name AS last_name,
        e.first_name AS first_name,
        e.middle_name AS middle_name,
        e.position AS position,
        e.phone AS phone,
        e.salary AS salary,
        e.hire_date AS hire_date,
        e.office AS office,
        d.name AS department_name
    FROM employee e
    JOIN department d ON e.department_id = d.department_id
    WHERE (:lastName IS NULL OR e.last_name ILIKE '%' || :lastName || '%')
      AND (:firstName IS NULL OR e.first_name ILIKE '%' || :firstName || '%')
      AND (:middleName IS NULL OR e.middle_name ILIKE '%' || :middleName || '%')
""", nativeQuery = true)
    List<Object[]> getEmployeeFullInfoRaw(
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("middleName") String middleName
    );

    default List<DtoEmployeeFullInfo> getEmployeeFullInfo(String lastName, String firstName, String middleName) {
        return getEmployeeFullInfoRaw(lastName, firstName, middleName).stream()
                .map(row -> new DtoEmployeeFullInfo(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        (String) row[5],
                        (BigDecimal) row[6],
                        row[7] != null ? ((Date) row[7]).toLocalDate() : null,
                        (String) row[8],
                        (String) row[9]
                ))
                .toList();
    }

    @Query(value = """
        SELECT 
            CONCAT(e.last_name, ' ', e.first_name) AS full_name,
            e.phone AS phone
        FROM employee e
        WHERE e.last_name = :lastName
    """, nativeQuery = true)
    List<Object[]> getEmployeeContactRaw(@Param("lastName") String lastName);

    default List<DtoEmployeeContact> getEmployeeContact(String lastName) {
        return getEmployeeContactRaw(lastName).stream()
                .map(row -> new DtoEmployeeContact(
                        (String) row[0],
                        (String) row[1]
                ))
                .toList();
    }

    @Query(value = """
        SELECT 
            d.name AS department_name,
            COUNT(e.employee_id) AS employee_count
        FROM department d
        LEFT JOIN employee e ON e.department_id = d.department_id
        GROUP BY d.name
        ORDER BY d.name
    """, nativeQuery = true)
    List<Object[]> getEmployeeCountByDepartmentRaw();

    default List<DtoDepartmentCount> getEmployeeCountByDepartment() {
        return getEmployeeCountByDepartmentRaw().stream()
                .map(row -> new DtoDepartmentCount(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }

    @Query(value = """
        SELECT 
            d.name AS department_name,
            COALESCE(SUM(e.salary), 0) AS salary_fund
        FROM department d
        LEFT JOIN employee e ON e.department_id = d.department_id
        GROUP BY d.name
        ORDER BY d.name
    """, nativeQuery = true)
    List<Object[]> getSalaryFundByDepartmentRaw();

    default List<DtoSalaryFund> getSalaryFundByDepartment() {
        return getSalaryFundByDepartmentRaw().stream()
                .map(row -> new DtoSalaryFund(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM employee
        WHERE employee_id IN (
            SELECT eh.employee_id
            FROM employee_history eh
            WHERE eh.change_type = 'TERMINATED'
        )
    """, nativeQuery = true)
    int deleteFiredEmployees();
}