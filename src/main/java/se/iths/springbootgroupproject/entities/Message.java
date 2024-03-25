package se.iths.springbootgroupproject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Column;


import java.time.LocalDate;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @CreatedDate
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
