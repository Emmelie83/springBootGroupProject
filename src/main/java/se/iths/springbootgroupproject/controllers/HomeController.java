package se.iths.springbootgroupproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.iths.springbootgroupproject.services.MessageService;

@Controller
public class HomeController {



    @GetMapping("/home")
    public String homePage(@RequestParam(name = "lang", defaultValue = "en") String lang) {
        return "home";
    }
}
