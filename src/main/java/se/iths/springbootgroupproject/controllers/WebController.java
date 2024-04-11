package se.iths.springbootgroupproject.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import se.iths.springbootgroupproject.CreateMessageFormData;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.User;
import se.iths.springbootgroupproject.services.MessageService;
import se.iths.springbootgroupproject.services.TranslationService;
import se.iths.springbootgroupproject.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {
    MessageService messageService;
    UserService userService;
    TranslationService translationService;

    public WebController(MessageService messageService, UserService userService, TranslationService translationService) {
        this.messageService = messageService;
        this.userService = userService;
        this.translationService = translationService;
    }

    @GetMapping("messages")
    public String getMessages(Model model, Principal principal, @AuthenticationPrincipal OAuth2User oauth2User,
                              @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "3") int size) {

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
        loggedInUser.ifPresent(user -> model.addAttribute("loggedInUser", user));

        return "messages";
    }

    @GetMapping("translation/{messageId}")
    public String translateMessage(@PathVariable Long messageId, Model model) {
        var messageOptional = messageService.findById(messageId);
        Message message = messageOptional.get();
        String translatedTitle = translationService.translateText(message.getMessageTitle());
        String translatedMessage = translationService.translateText(message.getMessageBody());
        String language = translationService.detectMessageLanguage(message.getMessageBody());
        model.addAttribute("postedLanguage", language);
        model.addAttribute("messageTitle", translatedTitle);
        model.addAttribute("messageBody", translatedMessage);
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
    public String updateMessage(@PathVariable Long messageId, Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        Message message = messageService.findById(messageId).get();

        String redirectUrl = redirectIfNotOwnerOrAdmin(oauth2User, message);
        if (redirectUrl != null) return redirectUrl;

        CreateMessageFormData formData = new CreateMessageFormData(message.getMessageTitle(), message.getMessageBody(), message.isPublic());
        model.addAttribute("formData", formData);
        model.addAttribute("originalMessage", message); // Add the original message to the model
        model.addAttribute("messageId", messageId);
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

    @RequestMapping("/user/data")
    public ModelAndView getUserSettingsPage(@AuthenticationPrincipal OAuth2User oauth2User) {
        ModelAndView modelAndView = new ModelAndView();
        if (oauth2User != null) {
            Integer githubId = oauth2User.getAttribute("id");
            var loggedInUser = userService.findByUserId(githubId);
            if (loggedInUser.isPresent()) {
                modelAndView.setViewName("userSettings");
                modelAndView.addObject("user", loggedInUser.get());
            } else {
                modelAndView.setViewName("errorPage");
            }
        } else {
            modelAndView.setViewName("errorPage");
        }
        return modelAndView;
    }

    @PostMapping("/user/update")
    public String updateUserProfile(@ModelAttribute User user, Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User != null) {
            Integer userId = (Integer) oauth2User.getAttribute("id");
            Optional<User> currentUserOptional = userService.findByUserId(userId);
            if (currentUserOptional.isPresent()) {
                User currentUser = currentUserOptional.get();
                currentUser.setFullName(user.getFullName());
                currentUser.setUserName(user.getUserName());
                currentUser.setEmail(user.getEmail());

                userService.saveUser(currentUser);

                model.addAttribute("updateSuccess", true);
                model.addAttribute("updateMessage", "User profile updated successfully.");

                return "redirect:/web/user/data";
            } else {
                return "redirect:/web/errorPage";
            }
        } else {
            return "redirect:/web/errorPage";
        }
    }

   /* @PostMapping("/user/uploadImage")
    public String uploadUserImage(@RequestParam("file") MultipartFile file, Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User != null) {
            Integer userId = (Integer) oauth2User.getAttribute("id");
            Optional<User> currentUserOptional = userService.findByUserId(userId);
            if (currentUserOptional.isPresent()) {
                User currentUser = currentUserOptional.get();

                if (!file.isEmpty()) {
                    try {
                        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                        Path uploadDir = Paths.get("src/main/resources/static/images");
                        if (!Files.exists(uploadDir)) {
                            Files.createDirectories(uploadDir);
                        }
                        Path filePath = uploadDir.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


                        currentUser.setAvatarUrl("/images/" + fileName);
                        userService.saveUser(currentUser);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "redirect:/user/data";
    }
*/


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
