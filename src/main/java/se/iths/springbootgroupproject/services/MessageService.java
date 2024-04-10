package se.iths.springbootgroupproject.services;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.PublicMessage;
import se.iths.springbootgroupproject.repos.MessageRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    //    @PreAuthorize("#updateMessage.user.getId() == authentication.principal.id")
    public Message updateMessage(Long id, Message updateMessage) {
        Message existingMessage = messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + id));
        if (updateMessage.getCreatedDate() != null) {
            existingMessage.setCreatedDate(updateMessage.getCreatedDate());
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

        existingMessage.setLastModifiedDate(LocalDate.now());


        existingMessage.setPublic(updateMessage.isPublic());

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

    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> fidAllByUserId(Long userId) {
        return messageRepository.findAllByUserId(userId);
    }
}
