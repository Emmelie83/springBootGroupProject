package se.iths.springbootgroupproject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.iths.springbootgroupproject.entities.PublicMessage;
import se.iths.springbootgroupproject.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private MessageService messageService;

    public RestApiController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public List<PublicMessage> getAllPublicMessages() {
        return messageService.findAllByPrivateMessageIsFalse();
    }
}
