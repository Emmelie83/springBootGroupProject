package se.iths.springbootgroupproject.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import se.iths.springbootgroupproject.entities.User;

import java.util.Optional;
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {


    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                String userName = ((OAuth2User) principal).getAttribute("login");
                return Optional.ofNullable(userName);
            }
        }
        return Optional.empty();
    }
}
