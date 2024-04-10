package se.iths.springbootgroupproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import se.iths.springbootgroupproject.config.SpringSecurityAuditorAware;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SpringBootGroupProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGroupProjectApplication.class, args);
    }


}
