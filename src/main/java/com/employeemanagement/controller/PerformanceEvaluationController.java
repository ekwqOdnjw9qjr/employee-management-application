package com.employeemanagement.controller;

import com.employeemanagement.dto.request.PerformanceEvaluationRequestDTO;
import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.PerformanceEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/evaluations")
@RequiredArgsConstructor
public class PerformanceEvaluationController {

    private final PerformanceEvaluationService evaluationService;
    private final EmployeeService employeeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("evaluations", evaluationService.getAllEvaluations());
        return "evaluations/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("evaluationRequest", new PerformanceEvaluationRequestDTO());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "evaluations/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("evaluationRequest") PerformanceEvaluationRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "evaluations/form";
        }

        evaluationService.createEvaluation(request);
        redirectAttributes.addFlashAttribute("success", "Evaluation created successfully");
        return "redirect:/evaluations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var evaluation = evaluationService.getEvaluationById(id);
        PerformanceEvaluationRequestDTO request = new PerformanceEvaluationRequestDTO();
        request.setEvaluationDate(evaluation.getEvaluationDate());
        request.setKpi(evaluation.getKpi());
        request.setScore(evaluation.getScore());
        request.setComment(evaluation.getComment());
        request.setEmployeeId(evaluation.getEmployeeId());
        request.setReviewerId(evaluation.getReviewerId());

        model.addAttribute("evaluationRequest", request);
        model.addAttribute("evaluationId", id);
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "evaluations/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("evaluationRequest") PerformanceEvaluationRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "evaluations/form";
        }

        evaluationService.updateEvaluation(id, request);
        redirectAttributes.addFlashAttribute("success", "Evaluation updated successfully");
        return "redirect:/evaluations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        evaluationService.deleteEvaluation(id);
        redirectAttributes.addFlashAttribute("success", "Evaluation deleted successfully");
        return "redirect:/evaluations";
    }
}