package se.iths.springbootgroupproject;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class MessageDTO {
    private String title;
    private String messageBody;
    private String author;
    private LocalDate date;

    public MessageDTO(String title, String messageBody, String author, LocalDate date) {
        this.title = title;
        this.messageBody = messageBody;
        this.author = author;
        this.date = date;
    }

    // Add getters and setters
}
