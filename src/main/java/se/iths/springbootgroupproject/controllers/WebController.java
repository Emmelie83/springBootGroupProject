package se.iths.springbootgroupproject.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.springbootgroupproject.CreateMessageFormData;
import se.iths.springbootgroupproject.services.MessageService;

@Controller
@RequestMapping("/web")
public class WebController {
    MessageService messageService;

    public WebController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("messages")
    public String getMessages(Model model) {
        var messages = messageService.findAllBy();
        model.addAttribute("messages", messages);

        return "messages";
    }

    @GetMapping("publicMessages")
    public String getPublicMessages(Model model) {
        var messages = messageService.findAllByPrivateMessageIsFalse();
        model.addAttribute("messages", messages);

        return "messages";
    }

    @GetMapping("create")
    public String postMessage(Model model) {
        CreateMessageFormData formData = new CreateMessageFormData();
        model.addAttribute("formData", formData);
        return "create";
    }


    @PostMapping("create")

    public String greetingSubmit(@Valid @ModelAttribute("formData") CreateMessageFormData message,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "create";
        }

        messageService.saveMessage(message.toEntity());
        return "redirect:/web/messages";
    }
}
