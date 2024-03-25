package se.iths.springbootgroupproject.services;

import se.iths.springbootgroupproject.entities.Message;
import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.entities.PublicMessage;
import se.iths.springbootgroupproject.repos.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message) {

        messageRepository.save(message);
        return message;
    }

    public List<Message> findAllBy() {
        return messageRepository.findAllBy();
    }

    public List<PublicMessage> findAllByPrivateMessageIsFalse() {
        return messageRepository.findAllByIsPublicIsTrue();
    }
}
