package se.iths.springbootgroupproject.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import se.iths.springbootgroupproject.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {



    @EntityGraph(attributePaths = "allUsers")
    List<User> findAllBy();

    Optional<User> findByGitId(Integer gitId);

    Optional<User> findByUserName(String userName);
}
