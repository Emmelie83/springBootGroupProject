package se.iths.springbootgroupproject.repos;

import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootgroupproject.entities.Message;

import java.util.List;


public interface MessageRepository extends ListCrudRepository<Message, Long> {

    List<Message>findByUser(String name);

    



}
