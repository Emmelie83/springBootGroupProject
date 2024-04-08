package se.iths.springbootgroupproject.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.iths.springbootgroupproject.entities.User;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return Optional.of((User) principal);
            }
        }
        return Optional.empty();
    }
}
