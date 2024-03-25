package se.iths.springbootgroupproject;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.repos.MessageRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.logging.Logger;
@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger LOG
            = Logger.getLogger(ApplicationRunner.class.getName());

    private final MessageRepository messageRepository;

    public StartupRunner(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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
            message.setUser("John Doe");
            message.setDate(LocalDate.now());
            message.setPublic(true);
            messageRepository.save(message);
        }
    }
}
