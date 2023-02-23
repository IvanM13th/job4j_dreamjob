package ru.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dreamjob.model.User;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class IndexController {

    /**
     * Сервер ищем нужный контроллер, который берет файл index.html
     * и возвращает его содержимое клиенту
     * @return возврашщает содержимое файла index.html
     */
    @GetMapping({"/", "/index"})
    public String getIndex(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        return "index";
    }

}