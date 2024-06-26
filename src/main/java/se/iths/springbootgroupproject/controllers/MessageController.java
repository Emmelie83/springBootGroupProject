package se.iths.springbootgroupproject.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.services.MessageService;

import java.util.List;


@RestController
public class MessageController {

    MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public Page<Message> getAllMessages() {
        Page<Message> messages = messageService.findAllBy(Pageable.unpaged());

        System.out.println("All messages are read from the database!");

        return messages;

    }

    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {

        return messageService.saveMessage(message);
    }
    @PutMapping("/messages/{id}")
    public Message updateMessage(@PathVariable Long id, @RequestBody Message message) {

        return messageService.updateMessage(id,message);
    }

}
