package com.employeemanagement.controller;

import com.employeemanagement.dto.request.TaskAssignmentRequestDTO;
import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.TaskAssignmentService;
import com.employeemanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class TaskAssignmentController {

    private final TaskAssignmentService assignmentService;
    private final EmployeeService employeeService;
    private final TaskService taskService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        return "assignments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("assignmentRequest", new TaskAssignmentRequestDTO());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("tasks", taskService.getAllTasks());
        return "assignments/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("assignmentRequest") TaskAssignmentRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("tasks", taskService.getAllTasks());
            return "assignments/form";
        }

        assignmentService.createAssignment(request);
        redirectAttributes.addFlashAttribute("success", "Task assignment created successfully");
        return "redirect:/assignments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var assignment = assignmentService.getAssignmentById(id);
        TaskAssignmentRequestDTO request = new TaskAssignmentRequestDTO();
        request.setRoleInTask(assignment.getRoleInTask());
        request.setAssignmentDate(assignment.getAssignmentDate());
        request.setEmployeeId(assignment.getEmployeeId());
        request.setTaskId(assignment.getTaskId());

        model.addAttribute("assignmentRequest", request);
        model.addAttribute("assignmentId", id);
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("tasks", taskService.getAllTasks());
        return "assignments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("assignmentRequest") TaskAssignmentRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("tasks", taskService.getAllTasks());
            return "assignments/form";
        }

        assignmentService.updateAssignment(id, request);
        redirectAttributes.addFlashAttribute("success", "Task assignment updated successfully");
        return "redirect:/assignments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        assignmentService.deleteAssignment(id);
        redirectAttributes.addFlashAttribute("success", "Task assignment deleted successfully");
        return "redirect:/assignments";
    }
}