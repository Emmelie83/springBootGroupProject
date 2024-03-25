package se.iths.springbootgroupproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.iths.springbootgroupproject.MessageDTO;
import se.iths.springbootgroupproject.repos.MessageRepository;

import java.util.stream.Collectors;

@Controller
public class WebController {

    MessageRepository messageRepository;

    public WebController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/web/messages")
    public String getMessages(Model model) {
        var messages = messageRepository.findAll();
        model.addAttribute("messages", messages);

        return "messages";
    // Todo: use a record instead of the whole object when adding to the model
    }

    @GetMapping("/web/publicMessages")
    public String getPublicMessages(Model model) {
        var messages = messageRepository.findAllByIsPublicIsTrue();
        model.addAttribute("messages", messages);

        return "messages";
    }
}
