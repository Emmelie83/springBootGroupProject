package se.iths.springbootgroupproject.entities;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Column;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @jakarta.persistence.Column(unique = true, name = "git_id")
    private Integer gitId;

    @Setter
    @jakarta.persistence.Column(unique = true, name = "user_name")
    private String userName;

    @Setter
    @Column("full_name")
    private String fullName;

    @Setter
    @jakarta.persistence.Column(unique = true, name = "email")
    private String email;

    @Setter
    @Column("avatar_url")
    private String avatarUrl;

    @Setter
    @Column("role")
    private String role;
}
