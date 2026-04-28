package com.employeemanagement.controller;

import com.employeemanagement.dto.request.TaskRequestDTO;
import com.employeemanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("taskRequest", new TaskRequestDTO());
        return "tasks/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("taskRequest") TaskRequestDTO request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tasks/form";
        }

        taskService.createTask(request);
        redirectAttributes.addFlashAttribute("success", "Task created successfully");
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var task = taskService.getTaskById(id);
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle(task.getTitle());
        request.setDescription(task.getDescription());
        request.setStartDate(task.getStartDate());
        request.setDeadline(task.getDeadline());
        request.setStatus(task.getStatus());

        model.addAttribute("taskRequest", request);
        model.addAttribute("taskId", id);
        return "tasks/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("taskRequest") TaskRequestDTO request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tasks/form";
        }

        taskService.updateTask(id, request);
        redirectAttributes.addFlashAttribute("success", "Task updated successfully");
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("success", "Task deleted successfully");
        return "redirect:/tasks";
    }
}