package ru.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dreamjob.model.Candidate;
import ru.dreamjob.model.Vacancy;
import ru.dreamjob.repository.CandidateRepository;
import ru.dreamjob.repository.MemoryCandidateRepository;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateRepository candidateRepository = MemoryCandidateRepository.getInstance();

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "candidates/create";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute("message", "Соискатель с таким id не найден");
            return "errors/404";
        }
        model.addAttribute("candidate", candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, Model model) {
        var isUpdated = candidateRepository.update(candidate);
        if (!isUpdated) {
            model.addAttribute("message", "Соискатель с указанным идентификатором не найден");
            return "errors/404";
        }
        return "redirect:/candidates";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = candidateRepository.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Соискатель с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/candidates";
    }
}