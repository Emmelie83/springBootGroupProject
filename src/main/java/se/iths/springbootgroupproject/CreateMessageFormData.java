package se.iths.springbootgroupproject;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import se.iths.springbootgroupproject.entities.Message;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
@Getter
@Setter
public class CreateMessageFormData {

    private LocalDate date;

    @NotNull
    @Size(min = 1, max = 100)
    private String messageTitle;
    @NotNull
    @Size(min = 1, max = 2000)
    private String messageBody;

    @Size(min = 1, max = 100)
    private String user;

    public CreateMessageFormData() {
    }

    public CreateMessageFormData(String messageTitle, String messageBody) {
        this.date = LocalDate.now();
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
        // this.user = AuthenticatedUser user;
        // this.isPublic = isPublic;
    }

    public Message toEntity() {
        var message = new Message();
        message.setDate(date);
        message.setMessageTitle(messageTitle);
        message.setMessageBody(messageBody);
        //message.setUser(user);
        return message;
    }
}
