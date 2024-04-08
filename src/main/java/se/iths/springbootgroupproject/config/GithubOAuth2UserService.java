package se.iths.springbootgroupproject.config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.repos.UserRepository;
import se.iths.springbootgroupproject.services.UserService;
import se.iths.springbootgroupproject.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    UserRepository userRepository;
    GitHubService gitHubService;

    public GithubOAuth2UserService(UserRepository userRepository,  GitHubService gitHubService) {
        this.userRepository = userRepository;
        this.gitHubService = gitHubService;
    }



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        Integer gitId = (Integer) attributes.get("id");
        Optional<User> authenticatedUser = userRepository.findByGitId(gitId);

        if (authenticatedUser.isEmpty()) {
            User user = createUserFromAttributes(attributes, userRequest);
            userRepository.save(user);
        }

        return oauth2User;
    }

    private User createUserFromAttributes(Map<String, Object> attributes, OAuth2UserRequest userRequest) {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        User user = new User();
        user.setUserName((String) attributes.get("login"));
        user.setAvatarUrl((String) attributes.get("avatar_url"));
        user.setFullName((String) attributes.get("name"));
        user.setEmail((String) attributes.get("email"));
        user.setGitId((Integer) attributes.get("id"));
        user.setRole("ROLE_USER");

        List<Email> result = gitHubService.getEmails(accessToken);
        for (Email email : result) {
            if (email.primary()) {
                String gitHubEmail = email.email();
                user.setEmail(gitHubEmail);
                break;
            }
        }

        return user;
    }


}