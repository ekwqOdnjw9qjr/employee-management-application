package com.employeemanagement.controller;


import com.employeemanagement.dto.request.EmployeeRequestDTO;
import com.employeemanagement.dto.response.EmployeeResponseDTO;
import com.employeemanagement.service.DepartmentService;
import com.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employeeRequest", new EmployeeRequestDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employees/form";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("employeeRequest") EmployeeRequestDTO employeeRequest,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (employeeRequest.getPhoto() == null || employeeRequest.getPhoto().isEmpty()) {
            employeeRequest.setPhoto("/images/default-avatar.png");
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                System.out.println("Validation error: " + error.getDefaultMessage());
            });
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/form";
        }

        try {
            redirectAttributes.addFlashAttribute("success", "Employee created successfully");
            return "redirect:/employees";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error creating employee: " + e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var employee = employeeService.getEmployeeById(id);
        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setLastName(employee.getLastName());
        request.setFirstName(employee.getFirstName());
        request.setMiddleName(employee.getMiddleName());
        request.setPosition(employee.getPosition());
        request.setPhone(employee.getPhone());
        request.setSalary(employee.getSalary());
        request.setHireDate(employee.getHireDate());
        request.setPhoto(employee.getPhoto());
        request.setOffice(employee.getOffice());
        request.setDepartmentId(employee.getDepartmentId());

        model.addAttribute("employeeRequest", request);
        model.addAttribute("employeeId", id);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employees/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("employeeRequest") EmployeeRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/form";
        }

        employeeService.updateEmployee(id, request);
        redirectAttributes.addFlashAttribute("success", "Employee updated successfully");
        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("success", "Employee deleted successfully");
        return "redirect:/employees";
    }
}