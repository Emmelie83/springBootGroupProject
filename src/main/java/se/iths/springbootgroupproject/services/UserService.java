package se.iths.springbootgroupproject.services;

import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.entities.User;
import se.iths.springbootgroupproject.repos.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser (User user){
        userRepository.save(user);
        return user;
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> findByUserId(Integer gitId) {
        return userRepository.findByGitId(gitId);

    }
}
