package se.iths.springbootgroupproject.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GitHubUser {
    private String login;
    private String userId;
    private String name;
    private String url;
    private String avatarUrl;
    private String email;

}
