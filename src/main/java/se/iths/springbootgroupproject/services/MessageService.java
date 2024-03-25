package se.iths.springbootgroupproject.services;

import jakarta.persistence.EntityNotFoundException;
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
    public Message updateMessage(Long id, Message updateMessage) {
        Message existingMessage = messageRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Message not found with id: "+id));
        if (updateMessage.getDate() != null) {
            existingMessage.setDate(updateMessage.getDate());
        }

        if (updateMessage.getMessageTitle() != null) {
            existingMessage.setMessageTitle(updateMessage.getMessageTitle());
        }

        if (updateMessage.getMessageBody() != null) {
            existingMessage.setMessageBody(updateMessage.getMessageBody());
        }

        if (updateMessage.getUser() != null) {
            existingMessage.setUser(updateMessage.getUser());
        }

        return messageRepository.save(existingMessage);
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
