package se.iths.springbootgroupproject.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootgroupproject.CreateMessageFormData;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.services.MessageService;

import java.time.LocalDate;

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
        formData.setDate(LocalDate.now());
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

    @GetMapping("update/{messageId}")
    public String updateMessage(@PathVariable Long messageId, Model model) {
        Message message = messageService.findById(messageId).get();
        CreateMessageFormData formData = new CreateMessageFormData();
        formData.setMessageTitle(message.getMessageTitle());
        formData.setMessageBody(message.getMessageBody());
        formData.setDate(LocalDate.now());
        model.addAttribute("formData", formData);
        model.addAttribute("originalMessage", message); // Add the original message to the model
        return "update";
    }

    @PostMapping("update/{messageId}")
    public String updateMessage(@PathVariable Long messageId, @Valid @ModelAttribute("formData") CreateMessageFormData message,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "update";
        }

        Message originalMessage = messageService.findById(messageId).get();

        messageService.updateMessage(messageId, originalMessage);

        return "redirect:/web/messages";
    }
}
