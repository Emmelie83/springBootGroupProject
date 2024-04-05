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
            message.setMessageTitle("Welcome to the circus");
            message.setMessageBody("Hello everybody");
            message.setUser("Kungen");
            message.setDate(LocalDate.now());
            message.setPublic(true);
            message.setLastModified(LocalDate.now());
            messageRepository.save(message);
        }
    }
}
