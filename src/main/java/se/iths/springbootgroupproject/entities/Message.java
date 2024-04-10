package se.iths.springbootgroupproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDate createdDate;

    private LocalDate lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @Column("title")
    private String messageTitle;

    @Column("message_body")
    private String messageBody;

    @ManyToOne()
    @JoinColumn(name = "user")
    private User user;

    @Column("is_public")
    private boolean isPublic = false;


}
