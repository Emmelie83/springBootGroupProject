package se.iths.springbootgroupproject;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import se.iths.springbootgroupproject.entities.Message;

@Getter
@Setter
public class CreateMessageFormData {

    @NotNull
    @Size(min = 1, max = 100)
    private String messageTitle;
    @NotNull
    @Size(min = 1, max = 2000)
    private String messageBody;

    public CreateMessageFormData() {
    }

    public CreateMessageFormData(String messageTitle, String messageBody) {
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;

    }

    public Message toEntity() {
        var message = new Message();
        message.setMessageTitle(messageTitle);
        message.setMessageBody(messageBody);
        return message;
    }
}
