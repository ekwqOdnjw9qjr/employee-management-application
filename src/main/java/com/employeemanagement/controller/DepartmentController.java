package com.employeemanagement.controller;


import com.employeemanagement.dto.request.DepartmentRequestDTO;
import com.employeemanagement.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("departmentRequest", new DepartmentRequestDTO());
        return "departments/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("departmentRequest") DepartmentRequestDTO request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "departments/form";
        }

        departmentService.createDepartment(request);
        redirectAttributes.addFlashAttribute("success", "Department created successfully");
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var department = departmentService.getDepartmentById(id);
        DepartmentRequestDTO request = new DepartmentRequestDTO();
        request.setName(department.getName());
        request.setFloor(department.getFloor());

        model.addAttribute("departmentRequest", request);
        model.addAttribute("departmentId", id);
        return "departments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("departmentRequest") DepartmentRequestDTO request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "departments/form";
        }

        departmentService.updateDepartment(id, request);
        redirectAttributes.addFlashAttribute("success", "Department updated successfully");
        return "redirect:/departments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        departmentService.deleteDepartment(id);
        redirectAttributes.addFlashAttribute("success", "Department deleted successfully");
        return "redirect:/departments";
    }
}