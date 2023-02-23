package ru.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.dreamjob.dto.FileDto;
import ru.dreamjob.model.User;
import ru.dreamjob.model.Vacancy;
import ru.dreamjob.service.CityService;
import ru.dreamjob.service.VacancyService;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;
    private final CityService cityService;

    public VacancyController(VacancyService vacancyService, CityService cityService) {
        this.vacancyService = vacancyService;
        this.cityService = cityService;
    }

    /**
     * @param model используется Thymeleaf для поиска объектов, которые нужно отобразить на виде
     * @return vacancies.html
     */
    @GetMapping
    public String getAll(Model model, HttpSession session) {
        getSessionInfo(model, session);
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, HttpSession session) {
        getSessionInfo(model, session);
        model.addAttribute("cities", cityService.findAll());
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy,
                         @RequestParam MultipartFile file,
                         Model model,
                         HttpSession session) {
        getSessionInfo(model, session);
        try {
            vacancyService.save(vacancy,
                    new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/vacancies";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, HttpSession session) {
        getSessionInfo(model, session);
        var vacancyOptional = vacancyService.findById(id);
        if (vacancyOptional.isEmpty()) {
            model.addAttribute("message", "Вакансия с таким id не найдена");
            return "errors/404";
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("vacancy", vacancyOptional.get());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy,
                         @RequestParam MultipartFile file,
                         Model model,
                         HttpSession session) {
        getSessionInfo(model, session);
        try {
            var isUpdated = vacancyService.update(vacancy,
                    new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Вакансия с таким id не неайдена");
                return "errors/404";
            }
            return "redirect:/vacancies";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id, HttpSession session) {
        getSessionInfo(model, session);
        var isDeleted = vacancyService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
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
