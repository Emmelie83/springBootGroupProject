package se.iths.springbootgroupproject.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.PublicMessage;

import java.util.List;


public interface MessageRepository extends ListPagingAndSortingRepository<Message, Long>, PagingAndSortingRepository<Message, Long>,ListCrudRepository<Message, Long> {
    Page<Message> findAllBy(Pageable pageable);

    List<PublicMessage> findAllByIsPublicIsTrue();


    List<Message> findAllByUserId(Long userId);

}
