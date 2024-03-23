package se.iths.springbootgroupproject.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.repos.MessageRepository;
import se.iths.springbootgroupproject.services.MessageService;

import java.util.List;


@RestController
public class MessageController {

    MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        List<Message> messages = messageService.findAllBy();

        System.out.println("All messages are read from the database!");

        return messages;

    }

    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {

        return messageService.saveMessage(message);
    }
}
