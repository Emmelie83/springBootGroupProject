package se.iths.springbootgroupproject;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Component;

import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.User;
import se.iths.springbootgroupproject.repos.MessageRepository;
import se.iths.springbootgroupproject.repos.UserRepository;
import se.iths.springbootgroupproject.services.MessageService;

import java.util.logging.Logger;
@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger LOG
            = Logger.getLogger(ApplicationRunner.class.getName());

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final MessageService messageService;

    public StartupRunner(MessageRepository messageRepository,
                         UserRepository userRepository, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Checking for Message");

        if (messageRepository.findAllBy(Pageable.unpaged()).isEmpty()) {
            LOG.info("Messages not found. Creating Messages");

            saveMessage("Eini", "Eini Enhörning", "eini@eteam.com","ROLE_USER", createMessage("Öl är gott", "Utan öl i tio dagar försmäktar jag i detta öde land."), true);
            saveMessage("Harry", "Harry Hare", "harry@eteam.com","ROLE_GUEST", createMessage("Drinking beer in the sun", "Spring is here and we are drinking beer in the sun."), true);
            saveMessage("Säli", "Säli Bukowski", "säli@eteam.com","ROLE_GUEST", createMessage("Svensk öl", "Här i Sverige är ölen svindyr. Precis som all annan alkohol."), true);
            saveMessage("Honey Bear", "Bear Brinkel", "bear@eteam.com", "ROLE_GUEST", createMessage("I don't like beer", "Why are you all writing about beer? I don't even like beer. I want to drink tea with honey."), true);
            saveMessage("Esi", "Esi Esel", "esi@eteam.com","ROLE_USER", createMessage("För liten för öl", "Jag är alldeles för liten för att dricka öl."), false);
            saveMessage("Eini", "Eini Enhörning", "eini@eteam.com","ROLE_USER", createMessage("Öl är livet", "Jag kommer att lära dig att dricka öl, Esi. Öl är livet!"), false);
            
        }
    }

    public Message createMessage(String title, String body) {
        Message message = new Message();
        message.setMessageTitle(title);
        message.setMessageBody(body);
        message.setLastModifiedDate(null);
        return message;
    }

    public void saveMessage(String userName, String fullName, String email, String role, Message message, boolean isPublic) {
        User user = userRepository.findByUserName(userName)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUserName(userName);
                    newUser.setFullName(fullName);
                    newUser.setEmail(email);
                    newUser.setRole(role);
                    return userRepository.save(newUser);
                });

        message.setUser(user);
        message.setPublic(isPublic);

        messageService.saveMessage(message);
    }
}
