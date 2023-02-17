package ru.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dreamjob.repository.MemoryVacancyRepository;
import ru.dreamjob.repository.VacancyRepository;

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
}