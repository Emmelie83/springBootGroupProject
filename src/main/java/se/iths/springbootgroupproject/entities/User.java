package se.iths.springbootgroupproject.entities;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Column;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.persistence.Column(unique = true, name = "git_id")
    private Integer gitId;

    @jakarta.persistence.Column(unique = true, name = "user_name")
    private String userName;

    @Column("full_name")
    private String fullName;

    @jakarta.persistence.Column(unique = true, name = "email")
    private String email;

    @Column("avatar_url")
    private String avatarUrl;

    @Column("role")
    private String role;
}
