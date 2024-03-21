package se.iths.springbootgroupproject.services;

import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.repos.UserRepository;
@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
