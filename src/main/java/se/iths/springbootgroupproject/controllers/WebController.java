package se.iths.springbootgroupproject.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.iths.springbootgroupproject.CreateMessageFormData;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.User;
import se.iths.springbootgroupproject.repos.MessageRepository;
import se.iths.springbootgroupproject.services.MessageService;
import se.iths.springbootgroupproject.services.TranslationService;
import se.iths.springbootgroupproject.services.UserService;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {
    MessageService messageService;
    UserService userService;
    TranslationService translationService;
    MessageRepository messageRepository;

    public WebController(MessageService messageService, UserService userService, TranslationService translationService, MessageRepository messageRepository) {
        this.messageService = messageService;
        this.userService = userService;
        this.translationService = translationService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("messages")
    public String getMessages(Model model, Principal principal, HttpServletRequest httpServletRequest, @AuthenticationPrincipal OAuth2User oauth2User,
                              @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> paginatedMessages = messageService.findPaginatedMessages(pageable);
        var publicMessages = messageService.findAllByPrivateMessageIsFalse();
        boolean isLoggedIn = principal != null;

        Integer githubId;
        Optional<User> loggedInUser = Optional.empty();

        if (oauth2User != null) {
            githubId = oauth2User.getAttribute("id");
            loggedInUser = userService.findByUserId(githubId);
        }

        model.addAttribute("messages", paginatedMessages);
        model.addAttribute("publicMessages", publicMessages);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("httpServletRequest", httpServletRequest);
        model.addAttribute("page", page);
        loggedInUser.ifPresent(user -> model.addAttribute("loggedInUser", user));

        return "messages";
    }

    @GetMapping("translation/{messageId}")
    public String translateMessage(@PathVariable Long messageId, Model model, HttpServletRequest httpServletRequest) {
        var messageOptional = messageService.findById(messageId);
        Message message = messageOptional.get();
        String translatedTitle = translationService.translateText(message.getMessageTitle());
        String translatedMessage = translationService.translateText(message.getMessageBody());
        String language = translationService.detectMessageLanguage(message.getMessageBody());
        model.addAttribute("postedLanguage", language);
        model.addAttribute("messageTitle", translatedTitle);
        model.addAttribute("messageBody", translatedMessage);
        model.addAttribute("httpServletRequest", httpServletRequest);
        return "translation";
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
        Integer githubId = oauth2User.getAttribute("id");
        var loggedInUser = userService.findByUserId(githubId);

        messageService.saveMessage(message.toEntity(loggedInUser.orElse(null)));

        return "redirect:/web/messages";
    }



    @GetMapping("update/{messageId}")
    public String updateMessage(@PathVariable Long messageId, Model model, HttpServletRequest httpServletRequest, @AuthenticationPrincipal OAuth2User oauth2User) {
        Message message = messageService.findById(messageId).get();

        String redirectUrl = redirectIfNotOwnerOrAdmin(oauth2User, message);
        if (redirectUrl != null) return redirectUrl;

        CreateMessageFormData formData = new CreateMessageFormData(message.getMessageTitle(), message.getMessageBody(), message.isPublic());
        model.addAttribute("formData", formData);
        model.addAttribute("originalMessage", message);
        model.addAttribute("messageId", messageId);
        model.addAttribute("httpServletRequest", httpServletRequest);
        return "update";
    }

    private String redirectIfNotOwnerOrAdmin(OAuth2User oauth2User, Message message) {
        if (oauth2User == null) {
            return "redirect:/web/messages";
        }

        Integer githubId = oauth2User.getAttribute("id");
        Optional<User> loggedInUser = userService.findByUserId(githubId);

        if (!Objects.equals(message.getUser().getGitId(), githubId) && (loggedInUser.isEmpty() || !loggedInUser.get().getRole().equals("ROLE_ADMIN"))) {
            return "redirect:/web/messages";
        }
        return null;
    }

    @PostMapping("update/{messageId}")
    public String greetingSubmit(@PathVariable Long messageId, @Valid @ModelAttribute("formData") CreateMessageFormData message,
                                 BindingResult bindingResult, @AuthenticationPrincipal OAuth2User oauth2User,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "update";
        }

        Message originalMessage = messageService.findById(messageId).get();
        originalMessage.setMessageTitle(message.getMessageTitle());
        originalMessage.setMessageBody(message.getMessageBody());
        originalMessage.setPublic(message.isMakePublic());

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
    public String getUserMessages(@RequestParam Long userId, Model model, HttpServletRequest httpServletRequest) {
        var userMessages = messageService.fidAllByUserId(userId);

        model.addAttribute("userMessages", userMessages);
        model.addAttribute("httpServletRequest", httpServletRequest);

        return "userMessages";
    }

    @GetMapping("/messages/{id}")
    public String oneMessage(@PathVariable Long id, Model model,
                             @AuthenticationPrincipal OAuth2User oauth2User,
                             @RequestParam("page") int page) {
        var message = messageRepository.findById(id).get();
        if (oauth2User != null) {
            Integer githubId = oauth2User.getAttribute("id");
            Optional<User> loggedInUser = userService.findByUserId(githubId);
            loggedInUser.ifPresent(user -> model.addAttribute("loggedInUser", user));
        }

        model.addAttribute("message", message);
        model.addAttribute("id", id);
        model.addAttribute("page", page);

        return "click-to-edit-default";
    }

    @PostMapping("/messages/{id}/edit")
    public String editForm(Model model, @PathVariable Long id,
                           @RequestParam("page") int page) {
        var message = messageRepository.findById(id).get();
        model.addAttribute("message", message);
        model.addAttribute("id", id);
        model.addAttribute("page", page);
        return "click-to-edit-form";
    }

    @PutMapping("/messages/{id}")
    public String editPost(@ModelAttribute Message message, Model model,
                           @RequestParam("page") int page,
                           @RequestParam(name = "isPublic", defaultValue = "false") boolean isPublic,
                           @PathVariable Long id) {
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + id));

        existingMessage.setMessageTitle(message.getMessageTitle());
        existingMessage.setMessageBody(message.getMessageBody());
        existingMessage.setPublic(isPublic);


         messageService.updateMessage(message.getId(), existingMessage);

        return "click-to-edit-default";
    }
}
