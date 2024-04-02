package se.iths.springbootgroupproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Column;


import java.time.LocalDate;

@Entity
@Getter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @CreatedDate
    @Column("date")
    private LocalDate date;

    @Setter
    @Column("title")
    private String messageTitle;

    @Setter
    @Column("message_body")
    private String messageBody;

    @Setter
    @Column("author")
    private String user;

    @Setter
    @Column("is_public")
    private boolean isPublic = false;


}
