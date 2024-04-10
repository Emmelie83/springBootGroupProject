package se.iths.springbootgroupproject.controllers;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootgroupproject.CreateMessageFormData;
import se.iths.springbootgroupproject.config.GithubOAuth2UserService;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.User;
import se.iths.springbootgroupproject.services.MessageService;
import se.iths.springbootgroupproject.services.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {
    MessageService messageService;
    UserService userService;

    public WebController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("messages")
    public String getMessages(Model model, Principal principal, @AuthenticationPrincipal OAuth2User oauth2User) {

        var messages = messageService.findAllBy();
        var publicMessages = messageService.findAllByPrivateMessageIsFalse();
        boolean isLoggedIn = principal != null;

        Integer githubId;
        Optional<User> loggedInUser = Optional.empty();

        if (oauth2User != null) {
            githubId = oauth2User.getAttribute("id");
            loggedInUser = userService.findByUserId(githubId);
        }

        model.addAttribute("messages", messages);
        model.addAttribute("publicMessages", publicMessages);
        model.addAttribute("isLoggedIn", isLoggedIn);
        loggedInUser.ifPresent(user -> model.addAttribute("loggedInUser", user));

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
                                 Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        Integer githubId = (Integer) oauth2User.getAttribute("id");
        var loggedInUser = userService.findByUserId(githubId);

        messageService.saveMessage(message.toEntity(loggedInUser.orElse(null)));

        return "redirect:/web/messages";
    }

    @GetMapping("update/{messageId}")
    public String updateMessage(@PathVariable Long messageId, Model model) {
        Message message = messageService.findById(messageId).get();

        CreateMessageFormData formData = new CreateMessageFormData(message.getMessageTitle(), message.getMessageBody(), message.isPublic());
        model.addAttribute("formData", formData);
        model.addAttribute("originalMessage", message); // Add the original message to the model
        model.addAttribute("messageId", messageId);
        return "update";
    }

    @PostMapping("update/{messageId}")
    public String greetingSubmit(@PathVariable Long messageId, @Valid @ModelAttribute("formData") CreateMessageFormData message,
                                BindingResult bindingResult,  @AuthenticationPrincipal OAuth2User oauth2User,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "update";
        }

        Message originalMessage = messageService.findById(messageId).get();
        originalMessage.setMessageTitle(message.getMessageTitle());
        originalMessage.setMessageBody(message.getMessageBody());
        originalMessage.setPublic(message.isMakePublic());
//        originalMessage.setCreatedDate(LocalDate.now());

        messageService.updateMessage(messageId, originalMessage);

        return "redirect:/web/messages";
    }

    @GetMapping("/web/messages")
    public String messagesPage(@RequestParam(name = "lang", defaultValue = "en") String lang, Model model) {
        String logoutButtonText;
        if ("en".equals(lang)) {
            logoutButtonText = "Logout";
        } else if ("sv".equals(lang)) {
            logoutButtonText = "Logga ut";
        } else {
            logoutButtonText = "Logout";
        }
        model.addAttribute("logoutButtonText", logoutButtonText);

        return "messages";
    }


    @GetMapping("userMessages")
    public String getUserMessages(@RequestParam Long userId, Model model) {
        var userMessages = messageService.fidAllByUserId(userId);

        model.addAttribute("userMessages", userMessages);

        return "userMessages";
    }
  
}
