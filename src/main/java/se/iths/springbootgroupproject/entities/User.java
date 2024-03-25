package se.iths.springbootgroupproject.entities;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column("user_name")
    private String userName;

    @Setter
    @Column("first_name")
    private String firstName;

    @Setter
    @Column("last_name")
    private String lastName;

    @Setter
    @Column("email")
    private String email;

}
