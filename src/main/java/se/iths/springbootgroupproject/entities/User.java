package se.iths.springbootgroupproject.entities;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    @Column("userName")
    private String userName;
    @Setter
    @Getter
    @Column("firstName")
    private String firstName;
    @Getter
    @Setter
    @Column("lastName")
    private String lastName;
    @Getter
    @Setter
    @Column("eMail")
    private String eMail;

    @OneToMany
    List<Message> messages = new ArrayList<>();


}
