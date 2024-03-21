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
import java.util.Locale;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    @Column ("Date")
    @CreatedDate
    private LocalDate date;
    @Getter
    @Setter
    @Column ("Title")
    private String messageTitle;
    @Getter
    @Setter
    @Column ("Message Body")
    private String messageBody;
    @Getter
    @Setter
    @Column("Author")
    private String user;

}
