package ru.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dreamjob.model.Vacancy;
import ru.dreamjob.repository.MemoryVacancyRepository;
import ru.dreamjob.repository.VacancyRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyRepository vacancyRepository = MemoryVacancyRepository.getInstance();

    /**
     * @param model используется Thymeleaf для поиска объектов, которые нужно отобразить на виде
     * @return vacancies.html
     */
    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vacancies", vacancyRepository.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy) {
        vacancyRepository.save(vacancy);
        return "redirect:/vacancies";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var vacancyOptional = vacancyRepository.findById(id);
        if (vacancyOptional.isEmpty()) {
            model.addAttribute("message", "Вакансия с таким id не найдена");
            return "errors/404";
        }
        model.addAttribute("vacancy", vacancyOptional.get());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, Model model) {
        var isUpdated = vacancyRepository.update(vacancy);
        if (!isUpdated) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = vacancyRepository.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }
}
