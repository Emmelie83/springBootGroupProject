package se.iths.springbootgroupproject.services;

import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.repos.MessageRepository;
@Service
public class MessageService {

    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
