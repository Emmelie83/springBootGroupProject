package se.iths.springbootgroupproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.iths.springbootgroupproject.services.MessageService;

@Controller
public class HomeController {

    MessageService messageService;

    public HomeController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("home")
    public String getPublicMessages(@RequestParam(name = "lang", defaultValue = "en") String lang, Model model) {
        var messages = messageService.findAllByPrivateMessageIsFalse();
        model.addAttribute("messages", messages);

        return "home";
    }

}
