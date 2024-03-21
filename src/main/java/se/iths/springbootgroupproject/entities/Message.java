package se.iths.springbootgroupproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Column;


import java.time.LocalDate;
import java.util.Locale;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column ("date")
    private LocalDate date;

    @Column ("title")
    private String messageTitle;

    @Column ("message_body")
    private String messageBody;

    @Column("author")
    private String user;

}
