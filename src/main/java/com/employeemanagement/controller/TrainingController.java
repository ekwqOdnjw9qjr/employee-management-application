package com.employeemanagement.controller;

import com.employeemanagement.dto.request.TrainingRequestDTO;
import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final EmployeeService employeeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("trainings", trainingService.getAllTrainings());
        return "trainings/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("trainingRequest", new TrainingRequestDTO());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "trainings/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("trainingRequest") TrainingRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "trainings/form";
        }

        trainingService.createTraining(request);
        redirectAttributes.addFlashAttribute("success", "Training created successfully");
        return "redirect:/trainings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        var training = trainingService.getTrainingById(id);
        TrainingRequestDTO request = new TrainingRequestDTO();
        request.setCourseName(training.getCourseName());
        request.setStartDate(training.getStartDate());
        request.setEndDate(training.getEndDate());
        request.setStatus(training.getStatus());
        request.setOrganization(training.getOrganization());
        request.setEmployeeId(training.getEmployeeId());

        model.addAttribute("trainingRequest", request);
        model.addAttribute("trainingId", id);
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "trainings/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("trainingRequest") TrainingRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "trainings/form";
        }

        trainingService.updateTraining(id, request);
        redirectAttributes.addFlashAttribute("success", "Training updated successfully");
        return "redirect:/trainings";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        trainingService.deleteTraining(id);
        redirectAttributes.addFlashAttribute("success", "Training deleted successfully");
        return "redirect:/trainings";
    }
}