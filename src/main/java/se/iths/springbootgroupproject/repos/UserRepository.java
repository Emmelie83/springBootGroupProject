package se.iths.springbootgroupproject.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootgroupproject.entities.User;
import java.util.List;
public interface UserRepository extends ListCrudRepository<User, Long> {
    List<User> findByName(String name);


    @EntityGraph(attributePaths = "allUsers")
    List<User> findAllBy();
}
