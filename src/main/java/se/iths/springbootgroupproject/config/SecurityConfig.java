package se.iths.springbootgroupproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

@Configuration
@EnableRetry
public class SecurityConfig {


    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/oauth/**", "/logout", "/error**").permitAll()
                        .requestMatchers("/web/create").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2Login(oath2-> oath2.defaultSuccessUrl("/web/messages", true));
        return http.build();
    }


    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n" +
                "ROLE_USER > ROLE_GUEST");
        return hierarchy;
    }

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }

}