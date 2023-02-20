package ru.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ThreadSafe
public class IndexController {

    /**
     * Сервер ищем нужный контроллер, который берет файл index.html
     * и возвращает его содержимое клиенту
     * @return возврашщает содержимое файла index.html
     */
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

}