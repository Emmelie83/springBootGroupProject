package se.iths.springbootgroupproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBootGroupProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGroupProjectApplication.class, args);
    }

}
