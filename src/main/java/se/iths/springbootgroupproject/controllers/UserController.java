package se.iths.springbootgroupproject.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.repos.UserRepository;
import se.iths.springbootgroupproject.entities.User;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        List<User> user = userRepository.findAllBy();

        System.out.println("All users are read from the database!");

        return user;

    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findAllBy().stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.getId();
        return userRepository.save(user);
    }
}
