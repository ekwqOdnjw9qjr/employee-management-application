package com.employeemanagement.repository;

import com.employeemanagement.entity.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

}