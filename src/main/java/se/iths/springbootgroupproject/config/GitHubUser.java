package se.iths.springbootgroupproject.config;

import lombok.Getter;

@Getter
public class GitHubUser {
    private String login;
    private String userId;
    private String name;
    private String url;
    private String avatarUrl;
    private String email;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
