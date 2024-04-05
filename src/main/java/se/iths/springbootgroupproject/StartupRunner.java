package se.iths.springbootgroupproject;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.User;
import se.iths.springbootgroupproject.repos.MessageRepository;
import org.springframework.stereotype.Component;
import se.iths.springbootgroupproject.repos.UserRepository;

import java.time.LocalDate;
import java.util.logging.Logger;
@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger LOG
            = Logger.getLogger(ApplicationRunner.class.getName());

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public StartupRunner(MessageRepository messageRepository,
                         UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Checking for Message");

        var result = messageRepository.findAllBy();
        if (result.isEmpty()) {
            LOG.info("Messages not found. Creating Messages");
            var message = new Message();
            message.setMessageTitle("Öl är gott");
            message.setMessageBody("Utan öl i tio dagar försmäktar jag i detta öde land.");
            User user;
            if (userRepository.findByUserName("Eini").isEmpty()) {
                user = new User();
                user.setUserName("Eini");
                user.setFullName("Eini Enhörning");
                user.setEmail("eini@eteam.se");
                userRepository.save(user);
            } else {
                user = userRepository.findByUserName("Eini").get();
            }
            message.setUser(user);
            message.setDate(LocalDate.now());
            message.setPublic(true);
            messageRepository.save(message);
        }
    }
}
