package se.iths.springbootgroupproject.config;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import se.iths.springbootgroupproject.services.UserService;
import se.iths.springbootgroupproject.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(GithubOAuth2UserService.class);

    GitHubService gitHubService;
    UserService userService;

    public GithubOAuth2UserService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    //https://dev.to/relive27/spring-security-oauth2-login-51lj
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();


        OAuth2AccessToken accessToken = userRequest.getAccessToken();


        List<Email> result = gitHubService.getEmails(accessToken);

        GitHubUser gitHubUser = new GitHubUser();
        gitHubUser.setUserId(String.valueOf(attributes.get("id")));
        gitHubUser.setName((String) attributes.get("name"));
        gitHubUser.setUrl((String) attributes.get("html_url"));
        gitHubUser.setAvatarUrl((String) attributes.get("avatar_url"));
        gitHubUser.setLogin((String) attributes.get("login"));
        gitHubUser.setEmail(result.getFirst().email());
        updateUser(gitHubUser);

        return oidcUser;
    }

    private void updateUser(GitHubUser gitUser) {


        if (userService.findByUserId(gitUser.getUserId()).isPresent()) {
            logger.info("User detected, {}, {}", gitUser.getLogin(), gitUser.getName());
            return;

        }
        logger.info("New user detected, {}, {}", gitUser.getLogin(), gitUser.getName());
        var user = userService.findByUserId(gitUser.getUserId()).get();
        user.setFullName(gitUser.getName());
        user.setAvatarUrl(gitUser.getAvatarUrl());
        user.setEmail(gitUser.getEmail());

        userService.saveUser(user);
    }
}