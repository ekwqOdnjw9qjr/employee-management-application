package com.employeemanagement.controller;

import com.employeemanagement.dto.request.EmployeeHistoryRequestDTO;
import com.employeemanagement.service.EmployeeHistoryService;
import com.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/histories")
@RequiredArgsConstructor
public class EmployeeHistoryController {

    private final EmployeeHistoryService historyService;
    private final EmployeeService employeeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("histories", historyService.getAllHistories());
        return "histories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("historyRequest", new EmployeeHistoryRequestDTO());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "histories/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("historyRequest") EmployeeHistoryRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "histories/form";
        }

        historyService.createHistory(request);
        redirectAttributes.addFlashAttribute("success", "History record created successfully");
        return "redirect:/histories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var history = historyService.getHistoryById(id);
        EmployeeHistoryRequestDTO request = new EmployeeHistoryRequestDTO();
        request.setChangeDate(history.getChangeDate());
        request.setChangeType(history.getChangeType());
        request.setEmployeeId(history.getEmployeeId());

        model.addAttribute("historyRequest", request);
        model.addAttribute("historyId", id);
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "histories/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("historyRequest") EmployeeHistoryRequestDTO request,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "histories/form";
        }

        historyService.updateHistory(id, request);
        redirectAttributes.addFlashAttribute("success", "History record updated successfully");
        return "redirect:/histories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        historyService.deleteHistory(id);
        redirectAttributes.addFlashAttribute("success", "History record deleted successfully");
        return "redirect:/histories";
    }
}