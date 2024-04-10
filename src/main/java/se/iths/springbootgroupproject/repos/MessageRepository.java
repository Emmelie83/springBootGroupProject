package se.iths.springbootgroupproject.repos;

import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.PublicMessage;

import java.util.List;


public interface MessageRepository extends ListCrudRepository<Message, Long> {

    List<Message> findAllBy();

    List<PublicMessage> findAllByIsPublicIsTrue();


    List<Message> findAllByUserId(Long userId);
}
