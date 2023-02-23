package ru.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.dreamjob.dto.FileDto;
import ru.dreamjob.model.Candidate;
import ru.dreamjob.model.User;
import ru.dreamjob.service.CandidateService;
import ru.dreamjob.service.CityService;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidateService;
    private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        getSessionInfo(model, session);
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate,
                         @RequestParam MultipartFile file,
                         Model model, HttpSession session) {
        getSessionInfo(model, session);
        try {
            candidateService.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, HttpSession session) {
        getSessionInfo(model, session);
        model.addAttribute("cities", cityService.findAll());
        return "candidates/create";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, HttpSession session) {
        getSessionInfo(model, session);
        var candidateOptional = candidateService.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute("message", "Соискатель с таким id не найден");
            return "errors/404";
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("candidate", candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate,
                         @RequestParam MultipartFile file,
                         Model model, HttpSession session) {
        getSessionInfo(model, session);
        try {
            var isUpdated = candidateService.update(candidate,
                    new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Соискатель с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id, HttpSession session) {
        getSessionInfo(model, session);
        var isDeleted = candidateService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Соискатель с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/candidates";
    }

    private void getSessionInfo(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }
}