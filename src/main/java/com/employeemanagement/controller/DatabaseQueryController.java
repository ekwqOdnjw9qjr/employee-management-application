package com.employeemanagement.controller;

import com.employeemanagement.dto.DepartmentStatsDTO;
import com.employeemanagement.dto.EmployeeByFloorDTO;
import com.employeemanagement.dto.EmployeeInfoDTO;
import com.employeemanagement.dto.EmployeeOrderDTO;
import com.employeemanagement.dto.query.DtoEmployeeByFloor;
import com.employeemanagement.dto.query.DtoEmployeeFullInfo;
import com.employeemanagement.dto.query.DtoEmployeeOrder;
import com.employeemanagement.service.DatabaseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/queries")
@RequiredArgsConstructor
public class DatabaseQueryController {

    private final DatabaseQueryService queryService;

    @GetMapping
    public String showQueriesPage() {
        return "queries/index";
    }

    @GetMapping("/employees-ordered")
    public String getEmployeesOrdered(Model model) {
        model.addAttribute("results", queryService.getEmployeesOrdered());
        model.addAttribute("queryName", "Employees Ordered by Department");
        return "queries/results";
    }

    @GetMapping("/employees-by-floor")
    public String getEmployeesByFloor(@RequestParam Integer floor, Model model) {
        model.addAttribute("results", queryService.getEmployeesByFloor(floor));
        model.addAttribute("floor", floor);
        model.addAttribute("queryName", "Employees on Floor " + floor);
        return "queries/results";
    }

    @GetMapping("/employee-full-info")
    public String getEmployeeFullInfo(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            Model model) {

        if (lastName != null) {
            lastName = lastName.trim();
            if (lastName.isEmpty()) lastName = null;
        }
        if (firstName != null) {
            firstName = firstName.trim();
            if (firstName.isEmpty()) firstName = null;
        }
        if (middleName != null) {
            middleName = middleName.trim();
            if (middleName.isEmpty()) middleName = null;
        }

        model.addAttribute("results", queryService.getEmployeeFullInfo(lastName, firstName, middleName));
        model.addAttribute("queryName", "Employee Full Information");
        return "queries/results";
    }

    @GetMapping("/employee-contact")
    public String getEmployeeContact(@RequestParam String lastName, Model model) {
        model.addAttribute("results", queryService.getEmployeeContact(lastName));
        model.addAttribute("lastName", lastName);
        model.addAttribute("queryName", "Contact Information");

        return "queries/results";
    }
    @GetMapping("/employee-count")
    public String getEmployeeCountByDepartment(Model model) {
        model.addAttribute("results", queryService.getEmployeeCountByDepartment());
        model.addAttribute("queryName", "Employee Count by Department");
        return "queries/results";
    }

    @GetMapping("/salary-fund")
    public String getSalaryFund(Model model) {
        model.addAttribute("results", queryService.getSalaryFundByDepartment());
        model.addAttribute("queryName", "Salary Fund by Department");
        return "queries/results";
    }

    @PostMapping("/delete-fired")
    public String deleteFiredEmployees(RedirectAttributes redirectAttributes) {
        int deleted = queryService.deleteFiredEmployees();
        redirectAttributes.addFlashAttribute("success", "Deleted " + deleted + " fired employees");
        return "redirect:/queries";
    }

    @GetMapping("/procedure-ordered")
    public String callOrderingByFields(@RequestParam(defaultValue = "department") String orderBy, Model model) {
        List<EmployeeOrderDTO> results = queryService.callOrderingByFields(orderBy);
        model.addAttribute("results", results);
        model.addAttribute("orderBy", orderBy);
        return "queries/procedure-ordered";
    }

    @GetMapping("/procedure-floor")
    public String callFindEmployeesByFloor(@RequestParam Integer floor, Model model) {
        List<EmployeeByFloorDTO> results = queryService.callFindEmployeesByFloor(floor);
        model.addAttribute("results", results);
        model.addAttribute("floor", floor);
        return "queries/procedure-floor";
    }

    @GetMapping("/procedure-employee-info")
    public String callGetEmployeeInfo(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            Model model) {

        if (lastName != null) {
            lastName = lastName.trim();
            if (lastName.isEmpty()) lastName = null;
        }
        if (firstName != null) {
            firstName = firstName.trim();
            if (firstName.isEmpty()) firstName = null;
        }
        if (middleName != null) {
            middleName = middleName.trim();
            if (middleName.isEmpty()) middleName = null;
        }

        List<EmployeeInfoDTO> results = queryService.callGetEmployeeInfo(lastName, firstName, middleName);
        model.addAttribute("results", results);
        model.addAttribute("queryName", "Procedure: get_employee_info");
        return "queries/procedure-employee-info";
    }

    @GetMapping("/procedure-stats")
    public String callCalculateDepartmentStats(Model model) {
        DepartmentStatsDTO stats = queryService.callCalculateDepartmentStats();
        model.addAttribute("stats", stats);
        model.addAttribute("queryName", "Procedure: calculate_department_stats");
        return "queries/results";
    }
}